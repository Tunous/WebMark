package me.thanel.webmark.work

import android.content.Context
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.Operation
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkRequest
import androidx.work.WorkerParameters
import me.thanel.webmark.data.Database
import org.kodein.di.generic.instance
import java.util.concurrent.TimeUnit

open class CleanupDatabaseWorker(
    appContext: Context,
    workerParams: WorkerParameters
) : BaseWorker(appContext, workerParams) {

    private val database: Database by instance()

    override suspend fun doWork(): Result {
        database.webmarkQueries.cleanup()
        return Result.success()
    }

    companion object {
        private const val WORK_NAME_AFTER_DELETE_CLEANUP = "webmarkAfterDeleteCleanup"
        private const val WORK_NAME_DAILY_CLEANUP = "webmarkDailyCleanup"

        fun enqueuePeriodic(context: Context): Pair<WorkRequest, Operation> {
            val constraints = Constraints.Builder()
                .setRequiresCharging(true)
                .build()
            val request = PeriodicWorkRequestBuilder<CleanupDatabaseWorker>(1, TimeUnit.DAYS)
                .setConstraints(constraints)
                .build()
            val operation = WorkManager.getInstance(context)
                .enqueueUniquePeriodicWork(
                    WORK_NAME_DAILY_CLEANUP,
                    ExistingPeriodicWorkPolicy.KEEP,
                    request
                )
            return request to operation
        }

        fun enqueueDelayed(context: Context): Pair<WorkRequest, Operation> {
            val request = OneTimeWorkRequestBuilder<CleanupDatabaseWorker>()
                .setInitialDelay(1, TimeUnit.MINUTES)
                .build()
            val operation = WorkManager.getInstance(context)
                .enqueueUniqueWork(
                    WORK_NAME_AFTER_DELETE_CLEANUP,
                    ExistingWorkPolicy.REPLACE,
                    request
                )
            return request to operation
        }

        fun cancelNonPeriodic(context: Context): Operation {
            return WorkManager.getInstance(context).cancelUniqueWork(WORK_NAME_AFTER_DELETE_CLEANUP)
        }
    }
}
