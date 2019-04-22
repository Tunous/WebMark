package me.thanel.webmark

import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import me.thanel.webmark.data.testDatabaseModule
import me.thanel.webmark.ext.asApp
import org.junit.Before
import org.junit.Rule

abstract class BaseTest {

    @get:Rule
    val activityRule = ActivityTestRule<MainActivity>(MainActivity::class.java, true, false)

    @Before
    fun setup() {
        overrideDependencies()
        startActivity()
    }

    private fun overrideDependencies() {
        val app = InstrumentationRegistry.getInstrumentation().targetContext.asApp()
        app.resetInjection()
        app.kodein.addImport(testDatabaseModule(app), true)
    }

    private fun startActivity() {
        activityRule.launchActivity(null)
    }
}
