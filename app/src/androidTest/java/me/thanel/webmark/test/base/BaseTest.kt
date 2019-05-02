package me.thanel.webmark.test.base

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.util.Log
import androidx.arch.core.executor.testing.CountingTaskExecutorRule
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.IdlingResource
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.platform.app.InstrumentationRegistry
import androidx.work.Configuration
import androidx.work.testing.SynchronousExecutor
import androidx.work.testing.WorkManagerTestInitHelper
import me.thanel.webmark.EspressoIdlingResource
import me.thanel.webmark.MainActivity
import me.thanel.webmark.ext.asApp
import me.thanel.webmark.preferences.WebMarkPreferences
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.kodein.di.DKodein
import org.kodein.di.DKodeinAware
import org.kodein.di.direct
import java.util.concurrent.TimeUnit

abstract class BaseTest : DKodeinAware {

    private lateinit var clipboardManager: ClipboardManager
    private var idlingResource: IdlingResource? = null
    protected lateinit var appContext: Context

    override lateinit var dkodein: DKodein

    @get:Rule
    val activityRule = IntentsTestRule<MainActivity>(MainActivity::class.java, false, false)

    @get:Rule
    val countingRule = CountingTaskExecutorRule()

    @Before
    open fun setup() {
        appContext = InstrumentationRegistry.getInstrumentation().targetContext
        dkodein = appContext.asApp().kodein.direct
        idlingResource = EspressoIdlingResource.idlingResource
        IdlingRegistry.getInstance().register(idlingResource)

        setupWorkManager()

        activityRule.runOnUiThread {
            clipboardManager = appContext.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        }

        clearPreferences()
        clearClipboard()
    }

    @After
    fun unregisterIdlingResource() {
        if (idlingResource != null) {
            IdlingRegistry.getInstance().unregister(idlingResource)
        }
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

    protected fun drain() {
        countingRule.drainTasks(5, TimeUnit.SECONDS)
    }

    private fun clearPreferences() {
        WebMarkPreferences.clear()
    }

    private fun setupWorkManager() {
        val config = Configuration.Builder()
            .setMinimumLoggingLevel(Log.DEBUG)
            .setExecutor(SynchronousExecutor())
            .build()
        WorkManagerTestInitHelper.initializeTestWorkManager(appContext, config)
    }
}
