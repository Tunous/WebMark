package me.thanel.webmark.data

import androidx.test.platform.app.InstrumentationRegistry
import com.squareup.sqldelight.Transacter
import com.squareup.sqldelight.android.AndroidSqliteDriver
import com.squareup.sqldelight.db.SqlDriver
import org.junit.Before
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton

abstract class DatabaseTest<T : Transacter>(
    private val getQueries: (Database) -> T
) {

    private lateinit var database: Database

    protected lateinit var db: T
        private set

    @Before
    fun prepareDatabase() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        val kodein = Kodein.direct {
            import(databaseModule(appContext))
            bind<SqlDriver>(overrides = true) with singleton {
                AndroidSqliteDriver(Database.Schema, appContext)
            }
        }
        database = kodein.instance()
        db = getQueries(database)
    }
}
