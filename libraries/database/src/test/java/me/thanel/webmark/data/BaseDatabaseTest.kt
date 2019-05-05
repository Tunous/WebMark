package me.thanel.webmark.data

import android.content.Context
import android.net.Uri
import androidx.test.core.app.ApplicationProvider
import org.junit.Before
import org.junit.runner.RunWith
import org.kodein.di.Kodein
import org.kodein.di.generic.instance
import org.robolectric.RobolectricTestRunner
import java.util.Calendar
import java.util.Date

@RunWith(RobolectricTestRunner::class)
abstract class BaseDatabaseTest {

    protected lateinit var db: WebmarkQueries
        private set

    @Before
    fun prepareDatabase() {
        val context =
            ApplicationProvider.getApplicationContext<Context>()
        val kodein = Kodein.direct {
            import(databaseModule(context))
            import(testDatabaseModule(context), allowOverride = true)
        }
        db = kodein.instance<Database>().webmarkQueries
    }

    protected fun todayPlusDays(amount: Int): Date {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DATE, amount)
        return calendar.time
    }

    protected fun insert(id: Long? = null, url: String, title: String? = null) {
        db.insert(id, Uri.parse(url))
        if (title != null) {
            val insertedId = id ?: db.lastInsertId().executeAsOne()
            db.updateById(
                id = insertedId,
                title = title,
                faviconUrl = null,
                estimatedReadingTimeMinutes = 0,
                imageUrl = null
            )
        }
    }
}
