package me.thanel.webmark

import android.app.Application
import androidx.annotation.VisibleForTesting
import androidx.appcompat.app.AppCompatDelegate
import com.chibatching.kotpref.Kotpref
import me.thanel.webmark.data.databaseModule
import me.thanel.webmark.preferences.WebMarkPreferences
import me.thanel.webmark.ui.imageloader.GlideImageLoader
import me.thanel.webmark.ui.imageloader.ImageLoader
import me.thanel.webmark.work.CleanupDatabaseWorker
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.androidXModule
import org.kodein.di.conf.ConfigurableKodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.singleton
import timber.log.Timber

@Suppress("unused")
class WebMarkApp : Application(), KodeinAware {

    override val kodein = ConfigurableKodein(mutable = true)

    @VisibleForTesting
    internal fun resetInjection() {
        kodein.clear()
        kodein.addImport(androidXModule(this@WebMarkApp))
        kodein.addImport(databaseModule(this@WebMarkApp))
        kodein.addConfig {
            bind<ImageLoader>() with singleton { GlideImageLoader }
        }
    }

    override fun onCreate() {
        super.onCreate()
        resetInjection()
        setupLogging()
        Kotpref.init(this)
        setupTheme()
        CleanupDatabaseWorker.enqueuePeriodic()
    }

    private fun setupLogging() {
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }

    private fun setupTheme() {
        val nightMode = when {
            WebMarkPreferences.useDarkTheme -> AppCompatDelegate.MODE_NIGHT_YES
            else -> AppCompatDelegate.MODE_NIGHT_NO
        }
        AppCompatDelegate.setDefaultNightMode(nightMode)
    }

}
