package me.thanel.webmark.core.base

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.platform.app.InstrumentationRegistry
import me.thanel.webmark.MainActivity
import me.thanel.webmark.data.testDatabaseModule
import me.thanel.webmark.ext.asApp
import me.thanel.webmark.preferences.WebMarkPreferences
import me.thanel.webmark.ui.imageloader.ImageLoader
import org.junit.Before
import org.junit.Rule
import org.kodein.di.DKodein
import org.kodein.di.DKodeinAware
import org.kodein.di.direct
import org.kodein.di.generic.bind
import org.kodein.di.generic.singleton

abstract class BaseTest : DKodeinAware {

    protected lateinit var appContext: Context

    override lateinit var dkodein: DKodein

    private val clipboardManager: ClipboardManager
        get() = appContext.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager

    @get:Rule
    val activityRule = IntentsTestRule<MainActivity>(MainActivity::class.java, false, false)

    @Before
    open fun setup() {
        appContext = InstrumentationRegistry.getInstrumentation().targetContext
        clearPreferences()
        clearClipboard()
        overrideDependencies()
    }

    protected fun clearClipboard() {
        clipboardManager.primaryClip = ClipData.newPlainText("", "")
    }

    protected fun setClipboardText(text: String) {
        clipboardManager.primaryClip = ClipData.newPlainText("Copied text", text)
    }

    private fun overrideDependencies() {
        val app = appContext.asApp()
        app.resetInjection()
        app.kodein.addImport(testDatabaseModule(app), true)
        app.kodein.addConfig {
            bind<ImageLoader>(overrides = true) with singleton { TestImageLoader }
        }
        dkodein = app.kodein.getOrConstruct().direct
    }

    protected fun startActivity() {
        activityRule.launchActivity(null)
    }

    private fun clearPreferences() {
        WebMarkPreferences.clear()
    }
}
