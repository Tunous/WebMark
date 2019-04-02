package me.thanel.webmark.saveaction

import android.app.IntentService
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.annotation.StringRes
import com.chimbori.crux.articles.ArticleExtractor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import me.thanel.webmark.Database
import me.thanel.webmark.R
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
            if (url == null) {
                showToast(R.string.info_invalid_url)
                return@runBlocking
            }

            if (checkIsAlreadySaved(url)) {
                showToast(R.string.info_duplicate_url)
            } else {
                saveWebmark(url)
            }

        }
    }

    private fun checkIsAlreadySaved(uri: Uri): Boolean {
        val queries = database.webmarkQueries
        val existingId = queries.selectIdForUrl(uri).executeAsOneOrNull() ?: return false

        // If already saved mark the link as new to update its position on list
        queries.markAsNewById(existingId)
        return true
    }

    private suspend fun saveWebmark(uri: Uri) {
        val queries = database.webmarkQueries
        val id = queries.transactionWithResult {
            queries.insert(null, uri)
            queries.lastInsertId().executeAsOne()
        }

        if (id == null) {
            showToast(R.string.error_saving)
            return
        }
        showToast(R.string.info_saved)

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
    }

    private suspend fun showToast(@StringRes messageResId: Int, length: Int = Toast.LENGTH_SHORT) =
        withContext(Dispatchers.Main) {
            Toast.makeText(this@SaveWebmarkService, messageResId, length).show()
        }

    companion object {
        private const val EXTRA_URL = "url"

        fun start(context: Context, uri: Uri) {
            val intent = Intent(context, SaveWebmarkService::class.java).apply {
                putExtra(EXTRA_URL, uri)
            }
            context.startService(intent)
        }
    }
}
