package me.thanel.webmark.list

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.longClick
import androidx.test.espresso.assertion.ViewAssertions.doesNotExist
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import me.thanel.webmark.R
import me.thanel.webmark.test.base.SampleDataTest
import me.thanel.webmark.test.data.LINK_ARCHIVED_WEBMARK
import me.thanel.webmark.test.data.LINK_WEBMARK
import me.thanel.webmark.test.data.TITLE_ARCHIVED_WEBMARK
import me.thanel.webmark.test.data.TITLE_WEBMARK
import me.thanel.webmark.test.matcher.chooserIntent
import me.thanel.webmark.test.matcher.onViewInPopup
import me.thanel.webmark.test.matcher.shareIntent
import me.thanel.webmark.test.matcher.stubExternalIntents
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class WebmarkPopupTest : SampleDataTest() {

    @Test
    fun displays_popup_menu_when_long_pressed_on_webmark() {
        drain()
        onView(withText(TITLE_WEBMARK)).perform(longClick())

        onViewInPopup(withText(TITLE_WEBMARK)).check(matches(isDisplayed()))
        onViewInPopup(withText(R.string.action_archive)).check(matches(isDisplayed()))
        onViewInPopup(withText(R.string.action_delete)).check(matches(isDisplayed()))
        onViewInPopup(withText(R.string.action_share_link)).check(matches(isDisplayed()))
    }

    @Test
    fun displays_popup_menu_when_long_pressed_on_archived_webmark() {
        onView(withId(R.id.archiveToggleButton)).perform(click())
        drain()
        onView(withText(TITLE_ARCHIVED_WEBMARK)).perform(longClick())

        onViewInPopup(withText(TITLE_ARCHIVED_WEBMARK)).check(matches(isDisplayed()))
        onViewInPopup(withText(R.string.action_unarchive)).check(matches(isDisplayed()))
        onViewInPopup(withText(R.string.action_delete)).check(matches(isDisplayed()))
        onViewInPopup(withText(R.string.action_share_link)).check(matches(isDisplayed()))
    }

    @Test
    fun can_archive_webmark_from_popup() {
        drain()
        onView(withText(TITLE_WEBMARK)).perform(longClick())
        onViewInPopup(withText(R.string.action_archive)).perform(click())
        drain()

        onView(withText(TITLE_WEBMARK)).check(doesNotExist())

        val webmark = queries.selectArchived(TITLE_WEBMARK).executeAsOne()
        assertNotNull("Webmark should be archived", webmark)
    }

    @Test
    fun can_delete_webmark_from_popup() {
        drain()
        onView(withText(TITLE_WEBMARK)).perform(longClick())
        onViewInPopup(withText(R.string.action_delete)).perform(click())
        drain()

        onView(withText(TITLE_WEBMARK)).check(doesNotExist())

        val allWebmarks = queries.selectAll().executeAsList()
        val webmark = allWebmarks.single { it.title == TITLE_WEBMARK }
        assertTrue("Webmark should be marked for deletion", webmark.markedForDeletion)
    }

    @Test
    fun can_share_webmark_link_from_popup() {
        stubExternalIntents()

        drain()
        onView(withText(TITLE_WEBMARK)).perform(longClick())
        onViewInPopup(withText(R.string.action_share_link)).perform(click())

        intended(chooserIntent(shareIntent(LINK_WEBMARK)))
    }

    @Test
    fun can_unarchive_archived_webmark_from_popup() {
        onView(withId(R.id.archiveToggleButton)).perform(click())
        drain()
        onView(withText(TITLE_ARCHIVED_WEBMARK)).perform(longClick())
        onViewInPopup(withText(R.string.action_unarchive)).perform(click())
        drain()

        onView(withText(TITLE_ARCHIVED_WEBMARK)).check(doesNotExist())

        val webmark = queries.selectUnarchived(TITLE_ARCHIVED_WEBMARK).executeAsOne()
        assertNotNull("Webmark should be unarchived", webmark)
    }

    @Test
    fun can_delete_archived_webmark_from_popup() {
        onView(withId(R.id.archiveToggleButton)).perform(click())
        drain()
        onView(withText(TITLE_ARCHIVED_WEBMARK)).perform(longClick())
        onViewInPopup(withText(R.string.action_delete)).perform(click())
        drain()

        onView(withText(TITLE_ARCHIVED_WEBMARK)).check(doesNotExist())

        val allWebmarks = queries.selectAll().executeAsList()
        val webmark = allWebmarks.single { it.title == TITLE_ARCHIVED_WEBMARK }
        assertTrue("Webmark should be marked for deletion", webmark.markedForDeletion)
    }

    @Test
    fun can_share_archived_webmark_link_from_popup() {
        stubExternalIntents()

        onView(withId(R.id.archiveToggleButton)).perform(click())
        drain()
        onView(withText(TITLE_ARCHIVED_WEBMARK)).perform(longClick())
        onViewInPopup(withText(R.string.action_share_link)).perform(click())

        intended(chooserIntent(shareIntent(LINK_ARCHIVED_WEBMARK)))
    }

    @Test
    fun can_undo_archive_action_performed_from_popup() {
        drain()
        onView(withText(TITLE_WEBMARK)).perform(longClick())
        onViewInPopup(withText(R.string.action_archive)).perform(click())
        drain()

        onView(withText(TITLE_WEBMARK)).check(doesNotExist())

        onView(withText(R.string.action_undo)).perform(click())
        drain()

        onView(withText(TITLE_WEBMARK)).check(matches(isDisplayed()))
    }

    @Test
    fun can_undo_delete_action_performed_from_popup() {
        drain()
        onView(withText(TITLE_WEBMARK)).perform(longClick())
        onViewInPopup(withText(R.string.action_delete)).perform(click())
        drain()

        onView(withText(TITLE_WEBMARK)).check(doesNotExist())

        onView(withText(R.string.action_undo)).perform(click())
        drain()

        onView(withText(TITLE_WEBMARK)).check(matches(isDisplayed()))
    }
}
