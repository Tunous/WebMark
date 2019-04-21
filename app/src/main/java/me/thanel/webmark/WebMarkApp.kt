package me.thanel.webmark

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.chibatching.kotpref.Kotpref
import me.thanel.webmark.data.databaseModule
import me.thanel.webmark.preferences.WebMarkPreferences
import me.thanel.webmark.work.CleanupDatabaseWorker
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.androidXModule
import timber.log.Timber

@Suppress("unused")
class WebMarkApp : Application(), KodeinAware {
    override val kodein by Kodein.lazy {
        import(androidXModule(this@WebMarkApp))
        import(databaseModule(this@WebMarkApp))
    }

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
        Kotpref.init(this)
        val nightMode = when {
            WebMarkPreferences.useDarkTheme -> AppCompatDelegate.MODE_NIGHT_YES
            else -> AppCompatDelegate.MODE_NIGHT_NO
        }
        AppCompatDelegate.setDefaultNightMode(nightMode)
        CleanupDatabaseWorker.enqueuePeriodic()
    }
}
