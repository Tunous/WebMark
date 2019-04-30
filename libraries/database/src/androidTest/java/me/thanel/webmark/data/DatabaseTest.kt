package me.thanel.webmark.data

import androidx.test.platform.app.InstrumentationRegistry
import com.squareup.sqldelight.Transacter
import org.junit.Before
import org.kodein.di.Kodein
import org.kodein.di.generic.instance

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
            import(testDatabaseModule(appContext), allowOverride = true)
        }
        database = kodein.instance()
        db = getQueries(database)
    }
}
