package me.thanel.webmark.work

import android.content.Context
import android.util.Log
import androidx.core.net.toUri
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.chimbori.crux.articles.ArticleExtractor
import com.chimbori.crux.images.ImageUrlExtractor
import com.chimbori.crux.urls.CruxURL
import kotlinx.coroutines.coroutineScope
import me.thanel.webmark.data.Database
import org.jsoup.Jsoup
import org.kodein.di.generic.instance

class ExtractWebmarkDetailsWorker(
    appContext: Context,
    workerParams: WorkerParameters
) : BaseWorker(appContext, workerParams) {

    private val database: Database by instance()

    override suspend fun doWork(): Result = coroutineScope {
        val id = inputData.getLong(KEY_ID, -1)
        if (id == -1L) {
            return@coroutineScope Result.failure()
        }

        val uri = database.webmarkQueries.selectUrlForId(id).executeAsOneOrNull()
            ?: return@coroutineScope Result.failure()

        try {
            val url = uri.toString()
            val document = try { Jsoup.connect(url).get() } catch (e: Exception) { null }
            val article = try {
                document?.let {
                    ArticleExtractor.with(url, it.html())
                        .extractMetadata()
                        .extractContent()
                        .estimateReadingTime()
                        .article()
                }
            } catch (e: Exception) {
                Log.e("ExtractWebmarkDetails", "Failed extracting content: ${e.localizedMessage}")
                null
            }

            val imageUrl = try {
                document?.let {
                    ImageUrlExtractor.with(url, document)
                        .findImage()
                        .imageUrl()
                }
            } catch (e: Exception) {
                Log.e("ExtractWebmarkDetails", "Failed extracting image: ${e.localizedMessage}")
                null
            }

            val alternativeImageUrl = if (CruxURL.parse(url).isLikelyImage) uri else null
            Log.d("ExtractWebmarkDetails", "Extracted image: $imageUrl - $alternativeImageUrl")

            database.webmarkQueries.updateById(
                id = id,
                title = article?.title,
                faviconUrl = article?.faviconUrl?.toUri(),
                estimatedReadingTimeMinutes = article?.estimatedReadingTimeMinutes ?: 0,
                imageUrl = imageUrl?.toUri() ?: alternativeImageUrl
            )

        } catch (e: Exception) {
            Log.e("ExtractWebmarkDetails", "Failed extracting details: ${e.localizedMessage}")
        }

        return@coroutineScope Result.success()
    }

    companion object {
        private const val KEY_ID = "id"

        fun enqueue(id: Long) {
            val constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()
            val request = OneTimeWorkRequestBuilder<ExtractWebmarkDetailsWorker>()
                .setInputData(workDataOf(KEY_ID to id))
                .setConstraints(constraints)
                .build()
            WorkManager.getInstance().enqueue(request)
        }
    }
}
