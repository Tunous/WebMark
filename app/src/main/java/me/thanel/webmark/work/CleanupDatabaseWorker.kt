package me.thanel.webmark.work

import android.content.Context
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import kotlinx.coroutines.coroutineScope
import me.thanel.webmark.data.Database
import org.kodein.di.generic.instance
import java.util.concurrent.TimeUnit

class CleanupDatabaseWorker(
    appContext: Context,
    workerParams: WorkerParameters
) : BaseWorker(appContext, workerParams) {

    private val database: Database by instance()

    override suspend fun doWork(): Result = coroutineScope {
        database.webmarkQueries.cleanup()
        return@coroutineScope Result.success()
    }

    companion object {
        private const val WORK_NAME_AFTER_DELETE_CLEANUP = "webmarkAfterDeleteCleanup"
        private const val WORK_NAME_DAILY_CLEANUP = "webmarkDailyCleanup"

        fun enqueuePeriodic() {
            val constraints = Constraints.Builder()
                .setRequiresCharging(true)
                .build()
            val request = PeriodicWorkRequestBuilder<CleanupDatabaseWorker>(1, TimeUnit.DAYS)
                .setConstraints(constraints)
                .build()
            WorkManager.getInstance()
                .enqueueUniquePeriodicWork(WORK_NAME_DAILY_CLEANUP, ExistingPeriodicWorkPolicy.KEEP, request)
        }

        fun enqueueDelayed() {
            val request = OneTimeWorkRequestBuilder<CleanupDatabaseWorker>()
                .setInitialDelay(1, TimeUnit.MINUTES)
                .build()
            WorkManager.getInstance()
                .enqueueUniqueWork(WORK_NAME_AFTER_DELETE_CLEANUP, ExistingWorkPolicy.REPLACE, request)
        }

        fun cancelNonPeriodic() {
            WorkManager.getInstance().cancelUniqueWork(WORK_NAME_AFTER_DELETE_CLEANUP)
        }
    }
}
