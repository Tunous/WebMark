package me.thanel.webmark

import android.app.IntentService
import android.content.Context
import android.content.Intent
import android.widget.Toast
import com.chimbori.crux.articles.ArticleExtractor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import me.thanel.webmark.ext.transactionWithResult
import org.jsoup.HttpStatusException
import org.jsoup.Jsoup
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance
import java.io.IOException
import java.net.SocketTimeoutException

class SaveWebmarkService : IntentService(SaveWebmarkService::class.java.simpleName), KodeinAware {

    override val kodein by kodein()

    private val database: Database by instance()

    override fun onHandleIntent(intent: Intent?) {
        val url = intent?.getStringExtra(EXTRA_URL)
        runBlocking {
            if (url != null) {
                showToast("Saving...")
                saveWebmark(url)
            } else {
                showToast("Invalid URL")
            }
        }
    }

    private suspend fun saveWebmark(url: String) {
        val queries = database.webmarkQueries
        val id = queries.transactionWithResult {
            queries.insert(null, url)
            queries.lastInsertId().executeAsOne()
        }

        if (id == null) {
            showToast("Error saving page")
            return
        }

        try {
            val document = Jsoup.connect(url).get()
            val article = ArticleExtractor.with(url, document.html())
                .extractMetadata()
                .article()

            queries.updateTitleById(article.title, id)

        } catch (e: HttpStatusException) {
            queries.updateTitleById("Http status error (${e.statusCode}) :(", id)
        } catch (e: SocketTimeoutException) {
            queries.updateTitleById("Timeout :(", id)
        } catch (e: IOException) {
            queries.updateTitleById("Error (${e.localizedMessage}) :(", id)
        }

        showToast("Saved page")
    }

    private suspend fun showToast(message: String, length: Int = Toast.LENGTH_SHORT) =
        withContext(Dispatchers.Main) {
            Toast.makeText(this@SaveWebmarkService, message, length).show()
        }

    companion object {
        private const val EXTRA_URL = "url"

        fun getIntent(context: Context, url: String) =
            Intent(context, SaveWebmarkService::class.java).apply {
                putExtra(EXTRA_URL, url)
            }
    }
}
