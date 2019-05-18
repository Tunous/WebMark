package me.thanel.webmark.work

import android.content.Context
import android.net.Uri
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.annotation.VisibleForTesting
import androidx.core.net.toUri
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.Operation
import androidx.work.WorkManager
import androidx.work.WorkRequest
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import me.thanel.webmark.R
import me.thanel.webmark.data.Database
import me.thanel.webmark.data.ext.transactionWithResult
import org.kodein.di.generic.instance
import timber.log.Timber

class SaveWebMarkWorker(
    private val appContext: Context,
    workerParams: WorkerParameters
) : BaseWorker(appContext, workerParams) {

    private val database: Database by instance()

    override suspend fun doWork(): Result {
        val url = inputData.getString(KEY_URI)?.toUri()
        if (url == null) {
            showToast(R.string.info_invalid_url)
            Timber.e("This shouldn't happen: Started save work without url as input")
            return Result.failure()
        }

        if (checkIsAlreadySaved(url)) {
            showToast(R.string.info_duplicate_url)
        } else {
            saveWebmark(url)
        }

        return Result.success()
    }

    private fun checkIsAlreadySaved(uri: Uri): Boolean {
        val queries = database.webMarkQueries
        val existingId = queries.selectIdForUrl(uri).executeAsOneOrNull() ?: return false

        // If already saved mark the link as new to update its position on list
        queries.markAsNewById(existingId)
        return true
    }

    private suspend fun saveWebmark(uri: Uri) {
        val queries = database.webMarkQueries
        val id = queries.transactionWithResult {
            queries.insert(null, uri)
            queries.lastInsertId().executeAsOneOrNull()
        }

        if (id == null) {
            showToast(R.string.error_saving)
            Timber.d("This shouldn't happen: Something went wrong while trying to save new webmark.")
        } else {
            showToast(R.string.info_saved)
            ExtractWebMarkDetailsWorker.enqueue(appContext, id)
        }
    }

    private suspend fun showToast(@StringRes messageResId: Int, length: Int = Toast.LENGTH_SHORT) =
        withContext(Dispatchers.Main) {
            Toast.makeText(appContext, messageResId, length).show()
        }

    companion object {
        private const val KEY_URI = "uri"

        @VisibleForTesting
        internal const val TAG = "save-webmark"

        fun enqueue(context: Context, uri: Uri): Pair<WorkRequest, Operation> {
            val request = OneTimeWorkRequestBuilder<SaveWebMarkWorker>()
                .setInputData(workDataOf(KEY_URI to uri.toString()))
                .addTag(TAG)
                .build()
            val operation = WorkManager.getInstance(context).enqueue(request)
            return request to operation
        }
    }
}
