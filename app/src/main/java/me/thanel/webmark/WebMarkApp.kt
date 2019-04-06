package me.thanel.webmark

import android.app.Application
import com.squareup.sqldelight.android.AndroidSqliteDriver
import com.squareup.sqldelight.db.SqlDriver
import me.thanel.webmark.columnadapter.DateColumnAdapter
import me.thanel.webmark.columnadapter.UriColumnAdapter
import me.thanel.webmark.data.Webmark
import me.thanel.webmark.preference.preferencesModule
import me.thanel.webmark.work.CleanupDatabaseWorker
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.androidXModule
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton

class WebMarkApp : Application(), KodeinAware {
    override val kodein by Kodein.lazy {
        import(androidXModule(this@WebMarkApp))
        import(preferencesModule())
        bind<SqlDriver>() with singleton {
            AndroidSqliteDriver(Database.Schema, this@WebMarkApp, "webmark.db")
        }
        bind<Webmark.Adapter>() with singleton {
            Webmark.Adapter(
                urlAdapter = UriColumnAdapter,
                faviconUrlAdapter = UriColumnAdapter,
                readAtAdapter = DateColumnAdapter,
                savedAtAdapter = DateColumnAdapter
            )
        }
        bind<Database>() with singleton { Database(instance(), instance()) }
    }

    override fun onCreate() {
        super.onCreate()
        CleanupDatabaseWorker.enqueuePeriodic()
    }
}
