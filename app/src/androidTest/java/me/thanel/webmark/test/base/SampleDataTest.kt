package me.thanel.webmark.test.base

import me.thanel.webmark.data.Database
import me.thanel.webmark.data.WebmarkQueries
import me.thanel.webmark.test.data.insertSampleData
import org.junit.Before
import org.kodein.di.generic.instance

abstract class SampleDataTest(private val autoStartActivity: Boolean = true) : BaseTest() {
    protected lateinit var queries: WebmarkQueries

    @Before
    override fun setup() {
        super.setup()

        val database = dkodein.instance<Database>()
        queries = database.webmarkQueries
        queries.deleteEverything()
        queries.insertSampleData()
        if (autoStartActivity) {
            startActivity()
        }
    }
}
