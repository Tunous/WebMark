package me.thanel.webmark.list

import androidx.core.net.toUri
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.doesNotExist
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withText
import me.thanel.webmark.R
import me.thanel.webmark.test.base.SampleDataTest
import me.thanel.webmark.test.data.LINK_NOT_SAVED
import org.hamcrest.CoreMatchers.startsWith
import org.junit.Assert.assertNotNull
import org.junit.Test

class SaveSuggestionTest : SampleDataTest(autoStartActivity = false) {

    private val questionText by lazy {
        appContext.getString(R.string.question_save_copied_url, LINK_NOT_SAVED)
    }

    @Test
    fun displays_save_suggestion_when_user_copied_link() {
        setClipboardText(LINK_NOT_SAVED)
        startActivity()

        onView(withText(questionText)).check(matches(isDisplayed()))
        onView(withText(R.string.action_save)).check(matches(isDisplayed()))
    }

    @Test
    fun can_save_suggested_link() {
        setClipboardText(LINK_NOT_SAVED)
        startActivity()

        onView(withText(R.string.action_save)).perform(click())

        onView(withText(LINK_NOT_SAVED)).check(matches(isDisplayed()))

        val savedWebmark = queries.selectAll().executeAsList().single { it.url == LINK_NOT_SAVED.toUri() }
        assertNotNull("New webmark should be saved", savedWebmark)
    }

    @Test
    fun does_not_display_save_suggestion_when_clipboard_is_empty() {
        clearClipboard()
        startActivity()

        onView(withText(startsWith(questionText.takeWhile { it != '?' }))).check(doesNotExist())
    }

    @Test
    fun does_not_display_save_suggestion_when_copied_non_link_text() {
        setClipboardText("non link text")
        startActivity()

        onView(withText(R.string.action_save)).check(doesNotExist())
        onView(withText(startsWith(questionText.takeWhile { it != '?' }))).check(doesNotExist())
    }
}
