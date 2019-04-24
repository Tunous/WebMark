package me.thanel.webmark.list

import android.content.Intent
import androidx.core.net.toUri
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.swipeLeft
import androidx.test.espresso.action.ViewActions.swipeRight
import androidx.test.espresso.assertion.ViewAssertions.doesNotExist
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers.hasAction
import androidx.test.espresso.intent.matcher.IntentMatchers.hasData
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import me.thanel.webmark.R
import me.thanel.webmark.test.base.SampleDataTest
import me.thanel.webmark.test.data.LINK_WEBMARK
import me.thanel.webmark.test.data.TITLE_ARCHIVED_WEBMARK
import me.thanel.webmark.test.data.TITLE_WEBMARK
import me.thanel.webmark.test.matcher.stubExternalIntents
import org.hamcrest.core.AllOf.allOf
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class WebmarkInteractionTest : SampleDataTest() {

    @Test
    fun clicking_on_webmark_redirects_to_browser() {
        stubExternalIntents()

        onView(withText(TITLE_WEBMARK)).perform(click())

        intended(allOf(hasAction(Intent.ACTION_VIEW), hasData(LINK_WEBMARK.toUri())))
    }

    @Test
    fun swiping_webmark_right_archives_it() {
        onView(withText(TITLE_WEBMARK)).perform(swipeRight())

        onView(withText(TITLE_WEBMARK)).check(doesNotExist())
        val guardiansWebmark = queries.selectRead(TITLE_WEBMARK).executeAsOne()
        assertNotNull("Webmark should be found as archived", guardiansWebmark)

        onView(withId(R.id.archiveToggleButton)).perform(click())

        onView(withText(TITLE_WEBMARK)).check(matches(isDisplayed()))
    }

    @Test
    fun swiping_archived_webmark_left_unarchives_it() {
        onView(withId(R.id.archiveToggleButton)).perform(click())
        onView(withText(TITLE_ARCHIVED_WEBMARK)).perform(swipeLeft())

        onView(withText(TITLE_ARCHIVED_WEBMARK)).check(doesNotExist())

        val guardiansWebmark = queries.selectUnread(TITLE_ARCHIVED_WEBMARK).executeAsOne()
        assertNotNull("Webmark should be found as unarchived", guardiansWebmark)

        onView(withId(R.id.archiveToggleButton)).perform(click())

        onView(withText(TITLE_ARCHIVED_WEBMARK)).check(matches(isDisplayed()))
    }

    @Test
    fun swiping_archived_webmark_right_deletes_it() {
        onView(withId(R.id.archiveToggleButton)).perform(click())
        onView(withText(TITLE_ARCHIVED_WEBMARK)).perform(swipeRight())

        onView(withText(TITLE_ARCHIVED_WEBMARK)).check(doesNotExist())

        onView(withId(R.id.archiveToggleButton)).perform(click())

        onView(withText(TITLE_ARCHIVED_WEBMARK)).check(doesNotExist())

        val allWebmarks = queries.selectAll().executeAsList()
        val guardiansWebmark = allWebmarks.single { it.title == TITLE_ARCHIVED_WEBMARK }
        assertTrue("Webmark should be marked for deletion", guardiansWebmark.markedForDeletion)
    }

    @Test
    fun archive_action_displays_snackbar_with_undo_option() {
        onView(withText(TITLE_WEBMARK)).perform(swipeRight())

        onView(withText(TITLE_WEBMARK)).check(doesNotExist())
        onView(withText(R.string.info_archived)).check(matches(isDisplayed()))

        onView(withText(R.string.action_undo)).perform(click())

        onView(withText(TITLE_WEBMARK)).check(matches(isDisplayed()))
    }

    @Test
    fun unarchive_action_displays_snackbar_with_undo_option() {
        onView(withId(R.id.archiveToggleButton)).perform(click())
        onView(withText(TITLE_ARCHIVED_WEBMARK)).perform(swipeLeft())

        onView(withText(TITLE_ARCHIVED_WEBMARK)).check(doesNotExist())
        onView(withText(R.string.info_unarchived)).check(matches(isDisplayed()))

        onView(withText(R.string.action_undo)).perform(click())

        onView(withText(TITLE_ARCHIVED_WEBMARK)).check(matches(isDisplayed()))
    }

    @Test
    fun delete_action_displays_snackbar_with_undo_option() {
        onView(withId(R.id.archiveToggleButton)).perform(click())
        onView(withText(TITLE_ARCHIVED_WEBMARK)).perform(swipeRight())

        onView(withText(TITLE_ARCHIVED_WEBMARK)).check(doesNotExist())
        onView(withText(R.string.info_deleted)).check(matches(isDisplayed()))

        onView(withText(R.string.action_undo)).perform(click())

        onView(withText(TITLE_ARCHIVED_WEBMARK)).check(matches(isDisplayed()))
    }
}

