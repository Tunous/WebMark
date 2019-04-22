package me.thanel.webmark.base

import android.content.Context
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.platform.app.InstrumentationRegistry
import me.thanel.webmark.MainActivity
import me.thanel.webmark.data.testDatabaseModule
import me.thanel.webmark.ext.asApp
import me.thanel.webmark.preferences.WebMarkPreferences
import org.junit.Before
import org.junit.Rule
import org.kodein.di.DKodein
import org.kodein.di.DKodeinAware
import org.kodein.di.direct

abstract class BaseTest : DKodeinAware {

    private lateinit var appContext: Context

    override lateinit var dkodein: DKodein

    @get:Rule
    val activityRule = IntentsTestRule<MainActivity>(MainActivity::class.java, false, false)

    @Before
    fun setup() {
        appContext = InstrumentationRegistry.getInstrumentation().targetContext
        clearPreferences()
        overrideDependencies()
    }

    private fun overrideDependencies() {
        val app = appContext.asApp()
        app.resetInjection()
        app.kodein.addImport(testDatabaseModule(app), true)
        dkodein = app.kodein.getOrConstruct().direct
    }

    protected fun startActivity() {
        activityRule.launchActivity(null)
    }

    private fun clearPreferences() {
        WebMarkPreferences.clear()
    }
}
