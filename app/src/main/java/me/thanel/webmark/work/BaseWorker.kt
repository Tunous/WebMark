package me.thanel.webmark.work

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein

abstract class BaseWorker(
    appContext: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(appContext, workerParams), KodeinAware {

    override val kodein by kodein(appContext)

}
