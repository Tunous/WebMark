package me.thanel.webmark.saveaction

import android.app.IntentService
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.widget.Toast
import com.chimbori.crux.articles.ArticleExtractor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import me.thanel.webmark.Database
import me.thanel.webmark.ext.transactionWithResult
import org.jsoup.Jsoup
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance

class SaveWebmarkService : IntentService(SaveWebmarkService::class.java.simpleName), KodeinAware {

    override val kodein by kodein()

    private val database: Database by instance()

    override fun onHandleIntent(intent: Intent?) {
        val url = intent?.getParcelableExtra<Uri>(EXTRA_URL)
        runBlocking {
            if (url != null) {
                showToast("Saving...")
                saveWebmark(url)
            } else {
                showToast("Invalid URL")
            }
        }
    }

    private suspend fun saveWebmark(uri: Uri) {
        val queries = database.webmarkQueries
        val id = queries.transactionWithResult {
            queries.insert(null, uri)
            queries.lastInsertId().executeAsOne()
        }

        if (id == null) {
            showToast("Error saving page")
            return
        }

        try {
            val url = uri.toString()
            val document = Jsoup.connect(url).get()
            val article = ArticleExtractor.with(url, document.html())
                .extractMetadata()
                .extractContent()
                .estimateReadingTime()
                .article()

            queries.updateById(
                id = id,
                title = article.title,
                faviconUrl = article.faviconUrl?.let(Uri::parse),
                estimatedReadingTimeMinutes = article.estimatedReadingTimeMinutes
            )

        } catch (e: Exception) {
            Log.e("SaveWebmarkService", "Failed extracting details: ${e.localizedMessage}")
        }

        showToast("Saved page")
    }

    private suspend fun showToast(message: String, length: Int = Toast.LENGTH_SHORT) =
        withContext(Dispatchers.Main) {
            Toast.makeText(this@SaveWebmarkService, message, length).show()
        }

    companion object {
        private const val EXTRA_URL = "url"

        fun getIntent(context: Context, uri: Uri) =
            Intent(context, SaveWebmarkService::class.java).apply {
                putExtra(EXTRA_URL, uri)
            }
    }
}
