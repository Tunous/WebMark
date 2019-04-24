package me.thanel.webmark.test.base

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.platform.app.InstrumentationRegistry
import me.thanel.webmark.MainActivity
import me.thanel.webmark.ext.asApp
import me.thanel.webmark.preferences.WebMarkPreferences
import org.junit.Before
import org.junit.Rule
import org.kodein.di.DKodein
import org.kodein.di.DKodeinAware
import org.kodein.di.direct

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
        dkodein = appContext.asApp().kodein.direct
        clearPreferences()
        clearClipboard()
    }

    protected fun clearClipboard() {
        clipboardManager.primaryClip = ClipData.newPlainText("", "")
    }

    protected fun setClipboardText(text: String) {
        clipboardManager.primaryClip = ClipData.newPlainText("Copied text", text)
    }

    protected fun startActivity() {
        activityRule.launchActivity(null)
    }

    private fun clearPreferences() {
        WebMarkPreferences.clear()
    }
}
