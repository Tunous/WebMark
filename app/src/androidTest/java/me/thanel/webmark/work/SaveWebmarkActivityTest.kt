package me.thanel.webmark.work

import android.content.Intent
import androidx.test.espresso.intent.rule.IntentsTestRule
import me.thanel.webmark.test.base.work.BaseWorkerTest
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

        assertWorkWithTagNotStarted(SaveWebmarkWorker.TAG)
    }

    @Test
    fun sharing_no_text_will_not_start_save_work() {
        val intent = Intent(Intent.ACTION_SEND)
        activityRule.launchActivity(intent)

        assertWorkWithTagNotStarted(SaveWebmarkWorker.TAG)
    }

    @Test
    fun launching_activity_without_intent_will_not_start_save_work() {
        activityRule.launchActivity(null)

        assertWorkWithTagNotStarted(SaveWebmarkWorker.TAG)
    }

    @Test
    fun sharing_url_will_start_save_work() {
        val intent = Intent(Intent.ACTION_SEND).apply {
            putExtra(Intent.EXTRA_TEXT, "https://example.com")
        }
        activityRule.launchActivity(intent)

        assertWorkWithTagStarted(SaveWebmarkWorker.TAG)
    }
}
