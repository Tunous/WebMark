package me.thanel.webmark.work

import android.content.Context
import androidx.annotation.VisibleForTesting
import androidx.core.net.toUri
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.chimbori.crux.articles.Article
import com.chimbori.crux.articles.ArticleExtractor
import com.chimbori.crux.images.ImageUrlExtractor
import com.chimbori.crux.urls.CruxURL
import me.thanel.webmark.data.Database
import me.thanel.webmark.ext.nullIfBlank
import okhttp3.OkHttpClient
import okhttp3.Request
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.kodein.di.generic.instance
import timber.log.Timber
import java.io.IOException

class ExtractWebmarkDetailsWorker(
    appContext: Context,
    workerParams: WorkerParameters
) : BaseWorker(appContext, workerParams) {

    private val database: Database by instance()

    override suspend fun doWork(): Result {
        val id = inputData.getLong(KEY_ID, -1L)
        check(id != -1L) { "Tried to start webmark details extraction task without valid id" }

        val uri = database.webmarkQueries.selectUrlForId(id).executeAsOneOrNull()
        if (uri == null) {
            Timber.w("Didn't find webmark for requested id: $id")
            return Result.failure()
        }

        val url = uri.toString()
        val document = try {
            downloadPageContent(url)
        } catch (e: IOException) {
            Timber.e(e, "Error while downloading webmark page")
            return Result.retry()
        }

        val article = document?.let {
            extractDetails(url, it)
        }
        val imageUrl = extractImage(url, document, article)
        val title = article?.title?.nullIfBlank()
        val faviconUrl = article?.faviconUrl?.toUri()
        val estimatedReadingTimeMinutes = article?.estimatedReadingTimeMinutes ?: 0
        val content = article?.document?.html()
        Timber.d(
            "Extracted webmark details: title=%s, faviconUrl=%s, estimatedReadingTimeMinutes=%d, imageUrl=%s, content=%s",
            title,
            faviconUrl,
            estimatedReadingTimeMinutes,
            imageUrl,
            content
        )

        database.webmarkQueries.updateById(
            id = id,
            title = title,
            faviconUrl = faviconUrl,
            estimatedReadingTimeMinutes = estimatedReadingTimeMinutes,
            imageUrl = imageUrl?.toUri(),
            content = content
        )

        return Result.success()
    }

    private fun extractDetails(url: String, document: Document): Article? {
        return try {
            ArticleExtractor.with(url, document.html())
                .extractMetadata()
                .extractContent()
                .estimateReadingTime()
                .article()
        } catch (e: Exception) {
            Timber.e(e, "Failed extracting webmark content")
            null
        }
    }

    private fun extractImage(url: String, document: Document?, article: Article?): String? {
        // First return url if it by itself is an image. That's the best candidate possible.
        if (CruxURL.parse(url).isLikelyImage) {
            return url
        }

        // Then try to return image parsed by article content extractor.
        val imageUrlFromArticle = article?.imageUrl
        if (imageUrlFromArticle != null) {
            return imageUrlFromArticle
        }

        // Finally try to extract image from document body.
        return document?.let {
            try {
                ImageUrlExtractor.with(url, it.body())
                    .findImage()
                    .imageUrl()
            } catch (e: Exception) {
                Timber.e(e, "Failed extracting webmark image")
                null
            }
        }
    }

    private fun downloadPageContent(url: String): Document? {
        val request = Request.Builder()
            .url(url)
            .build()

        OkHttpClient()
            .newCall(request)
            .execute()
            .use { response ->
                return response.body()?.byteStream()?.use { inputStream ->
                    Jsoup.parse(inputStream, null, url)
                }
            }
    }

    companion object {
        private const val KEY_ID = "id"

        @VisibleForTesting
        internal const val TAG = "extract-webmark-details"

        fun enqueue(context: Context, id: Long) {
            val constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()
            val request = OneTimeWorkRequestBuilder<ExtractWebmarkDetailsWorker>()
                .setInputData(workDataOf(KEY_ID to id))
                .setConstraints(constraints)
                .addTag(TAG)
                .build()
            WorkManager.getInstance(context).enqueue(request)
        }
    }
}
