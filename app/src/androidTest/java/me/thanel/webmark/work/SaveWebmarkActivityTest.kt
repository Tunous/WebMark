package me.thanel.webmark.work

import android.content.Intent
import androidx.test.espresso.intent.rule.IntentsTestRule
import me.thanel.webmark.test.base.work.BaseWorkerTest
import org.hamcrest.Matchers.empty
import org.hamcrest.Matchers.hasSize
import org.junit.Assert.assertThat
import org.junit.Rule
import org.junit.Test

class SaveWebmarkActivityTest : BaseWorkerTest() {

    @get:Rule
    val activityRule = IntentsTestRule<SaveWebmarkActivity>(SaveWebmarkActivity::class.java, false, false)

    @Test
    fun sharing_regular_text_will_not_start_save_work() {
        val intent = Intent(Intent.ACTION_SEND).apply {
            putExtra(Intent.EXTRA_TEXT, "Test text")
        }
        activityRule.launchActivity(intent)

        assertSaveWork(started = false)
    }

    @Test
    fun sharing_no_text_will_not_start_save_work() {
        val intent = Intent(Intent.ACTION_SEND)
        activityRule.launchActivity(intent)

        assertSaveWork(started = false)
    }

    @Test
    fun launching_activity_without_intent_will_not_start_save_work() {
        activityRule.launchActivity(null)

        assertSaveWork(started = false)
    }

    @Test
    fun sharing_url_will_start_save_work() {
        val intent = Intent(Intent.ACTION_SEND).apply {
            putExtra(Intent.EXTRA_TEXT, "https://example.com")
        }
        activityRule.launchActivity(intent)

        assertSaveWork(started = true)
    }

    private fun assertSaveWork(started: Boolean) {
        val workInfos = workManager.getWorkInfosByTag(SaveWebmarkWorker.TAG_SAVE_WEBMARK).get()
        if (started) {
            assertThat("Work should be started", workInfos, hasSize(1))
        } else {
            assertThat("Work shouldn't be started", workInfos, empty())
        }
    }
}
