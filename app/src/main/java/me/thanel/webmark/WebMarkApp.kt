package me.thanel.webmark

import android.app.Application
import com.chibatching.kotpref.Kotpref
import me.thanel.webmark.data.databaseModule
import me.thanel.webmark.ext.refreshDefaultTheme
import me.thanel.webmark.share.QuickShareDetailsProvider
import me.thanel.webmark.share.RealQuickShareDetailsProvider
import me.thanel.webmark.ui.imageloader.GlideImageLoader
import me.thanel.webmark.ui.imageloader.ImageLoader
import me.thanel.webmark.ui.popup.WebMarkPopupMenu
import me.thanel.webmark.work.CleanupDatabaseWorker
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.androidXModule
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton
import timber.log.Timber

@Suppress("unused")
open class WebMarkApp : Application(), KodeinAware {

    override val kodein: Kodein by Kodein.lazy {
        import(androidXModule(this@WebMarkApp))
        import(databaseModule(this@WebMarkApp))
        bind<ImageLoader>() with singleton { GlideImageLoader }
        bind<QuickShareDetailsProvider>() with singleton { RealQuickShareDetailsProvider(this@WebMarkApp) }
        bind<WebMarkPopupMenu>() with singleton { WebMarkPopupMenu(instance()) }
    }

    override fun onCreate() {
        super.onCreate()
        setupLogging()
        Kotpref.init(this)
        refreshDefaultTheme()
        CleanupDatabaseWorker.enqueuePeriodic(this)
    }

    private fun setupLogging() {
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}
