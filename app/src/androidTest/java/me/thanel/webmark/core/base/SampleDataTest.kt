package me.thanel.webmark.core.base

import me.thanel.webmark.core.data.insertSampleData
import me.thanel.webmark.data.Database
import me.thanel.webmark.data.WebmarkQueries
import org.junit.Before
import org.kodein.di.generic.instance

abstract class SampleDataTest(private val autoStartActivity: Boolean = true) : BaseTest() {
    protected lateinit var queries: WebmarkQueries

    @Before
    override fun setup() {
        super.setup()

        val database = dkodein.instance<Database>()
        queries = database.webmarkQueries
        queries.insertSampleData()
        if (autoStartActivity) {
            startActivity()
        }
    }
}
