package me.thanel.webmark

import android.app.Application
import com.squareup.sqldelight.android.AndroidSqliteDriver
import com.squareup.sqldelight.db.SqlDriver
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton

class WebMarkApp : Application(), KodeinAware {
    override val kodein by Kodein.lazy {
        bind<SqlDriver>() with singleton {
            AndroidSqliteDriver(Database.Schema, this@WebMarkApp, "webmark.db")
        }
        bind<Database>() with singleton { Database(instance()) }
    }
}
