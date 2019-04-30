package me.thanel.webmark.data

import android.content.Context
import androidx.annotation.VisibleForTesting
import com.squareup.sqldelight.android.AndroidSqliteDriver
import com.squareup.sqldelight.db.SqlDriver
import me.thanel.webmark.data.columnadapter.DateColumnAdapter
import me.thanel.webmark.data.columnadapter.UriColumnAdapter
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton

private const val DATABASE_NAME = "webmark.db"

fun databaseModule(appContext: Context) = Kodein.Module("database") {
    bind<SqlDriver>() with singleton {
        AndroidSqliteDriver(Database.Schema, appContext, DATABASE_NAME)
    }
    bind() from singleton { Database(instance(), instance()) }
    bind() from singleton {
        Webmark.Adapter(
            urlAdapter = UriColumnAdapter,
            faviconUrlAdapter = UriColumnAdapter,
            archivedAtAdapter = DateColumnAdapter,
            savedAtAdapter = DateColumnAdapter,
            imageUrlAdapter = UriColumnAdapter
        )
    }
}

@VisibleForTesting
fun testDatabaseModule(appContext: Context) = Kodein.Module("testDatabase") {
    bind<SqlDriver>(overrides = true) with singleton {
        AndroidSqliteDriver(Database.Schema, appContext)
    }
}
