package me.thanel.webmark.work

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.work.Constraints
import androidx.work.CoroutineWorker
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.chimbori.crux.articles.ArticleExtractor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import me.thanel.webmark.Database
import org.jsoup.Jsoup
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance

class ExtractWebmarkDetailsWorker(
    appContext: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(appContext, workerParams), KodeinAware {

    override val kodein by kodein(appContext)

    override val coroutineContext = Dispatchers.IO

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
            val document = Jsoup.connect(url).get()
            val article = ArticleExtractor.with(url, document.html())
                .extractMetadata()
                .extractContent()
                .estimateReadingTime()
                .article()

            database.webmarkQueries.updateById(
                id = id,
                title = article.title,
                faviconUrl = article.faviconUrl?.let(Uri::parse),
                estimatedReadingTimeMinutes = article.estimatedReadingTimeMinutes
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
