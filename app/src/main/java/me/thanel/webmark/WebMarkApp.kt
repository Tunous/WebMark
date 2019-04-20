package me.thanel.webmark

import android.app.Application
import com.chibatching.kotpref.Kotpref
import me.thanel.webmark.data.databaseModule
import me.thanel.webmark.work.CleanupDatabaseWorker
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.androidXModule

@Suppress("unused")
class WebMarkApp : Application(), KodeinAware {
    override val kodein by Kodein.lazy {
        import(androidXModule(this@WebMarkApp))
        import(databaseModule(this@WebMarkApp))
    }

    override fun onCreate() {
        super.onCreate()
        Kotpref.init(this)
        CleanupDatabaseWorker.enqueuePeriodic()
    }
}
