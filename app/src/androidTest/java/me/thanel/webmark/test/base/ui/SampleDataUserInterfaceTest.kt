package me.thanel.webmark.test.base.ui

import me.thanel.webmark.data.Database
import me.thanel.webmark.data.WebMarkQueries
import me.thanel.webmark.test.data.insertSampleData
import org.junit.Before
import org.kodein.di.generic.instance

abstract class SampleDataUserInterfaceTest(
    private val autoStartActivity: Boolean = true
) : BaseUserInterfaceTest() {

    protected lateinit var queries: WebMarkQueries

    @Before
    override fun setup() {
        super.setup()

        val database = dkodein.instance<Database>()
        queries = database.webMarkQueries
        queries.deleteEverything()
        queries.insertSampleData()
        if (autoStartActivity) {
            startActivity()
        }
    }
}
