package me.thanel.webmark.work

import android.content.Context
import android.net.Uri
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.core.net.toUri
import androidx.work.CoroutineWorker
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext
import me.thanel.webmark.Database
import me.thanel.webmark.R
import me.thanel.webmark.ext.transactionWithResult
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance

class SaveWebmarkWorker(
    private val appContext: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(appContext, workerParams), KodeinAware {

    override val kodein by kodein(appContext)

    override val coroutineContext = Dispatchers.IO

    private val database: Database by instance()

    override suspend fun doWork(): Result = coroutineScope {
        val url = inputData.getString(KEY_URI)?.toUri()
        if (url == null) {
            showToast(R.string.info_invalid_url)
            return@coroutineScope Result.failure()
        }

        if (checkIsAlreadySaved(url)) {
            showToast(R.string.info_duplicate_url)
        } else {
            saveWebmark(url)
        }

        return@coroutineScope Result.success()
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
            return@transactionWithResult queries.lastInsertId().executeAsOneOrNull()
        }

        if (id == null) {
            showToast(R.string.error_saving)
        } else {
            showToast(R.string.info_saved)
            ExtractWebmarkDetailsWorker.enqueue(id)
        }
    }

    private suspend fun showToast(@StringRes messageResId: Int, length: Int = Toast.LENGTH_SHORT) =
        withContext(Dispatchers.Main) {
            Toast.makeText(appContext, messageResId, length).show()
        }

    companion object {
        private const val KEY_URI = "uri"

        fun enqueue(uri: Uri) {
            val request = OneTimeWorkRequestBuilder<SaveWebmarkWorker>()
                .setInputData(workDataOf(KEY_URI to uri.toString()))
                .build()
            WorkManager.getInstance().enqueue(request)
        }
    }
}
