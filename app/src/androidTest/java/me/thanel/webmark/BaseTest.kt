package me.thanel.webmark

import android.content.Context
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import me.thanel.webmark.data.testDatabaseModule
import me.thanel.webmark.ext.asApp
import me.thanel.webmark.preferences.WebMarkPreferences
import org.junit.Before
import org.junit.Rule

abstract class BaseTest {

    private lateinit var appContext: Context

    @get:Rule
    val activityRule = ActivityTestRule<MainActivity>(MainActivity::class.java, true, false)

    @Before
    fun setup() {
        appContext = InstrumentationRegistry.getInstrumentation().targetContext
        clearPreferences()
        overrideDependencies()
        startActivity()
    }

    private fun overrideDependencies() {
        val app = appContext.asApp()
        app.resetInjection()
        app.kodein.addImport(testDatabaseModule(app), true)
    }

    private fun startActivity() {
        activityRule.launchActivity(null)
    }

    private fun clearPreferences() {
        WebMarkPreferences.clear()
    }
}
