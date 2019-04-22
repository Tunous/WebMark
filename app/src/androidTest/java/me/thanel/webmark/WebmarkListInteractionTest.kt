package me.thanel.webmark

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
import me.thanel.webmark.base.BaseTest
import me.thanel.webmark.data.Database
import me.thanel.webmark.data.LINK_GUARDIANS
import me.thanel.webmark.data.TITLE_ENDGAME_PRESELLS
import me.thanel.webmark.data.TITLE_GUARDIANS
import me.thanel.webmark.data.WebmarkQueries
import me.thanel.webmark.data.insertSampleData
import org.hamcrest.core.AllOf.allOf
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.kodein.di.generic.instance

class WebmarkListInteractionTest : BaseTest() {

    private lateinit var queries: WebmarkQueries

    @Before
    fun setupDatabase() {
        val database = dkodein.instance<Database>()
        queries = database.webmarkQueries
        queries.insertSampleData()
        startActivity()
    }

    @Test
    fun clicking_on_webmark_redirects_to_browser() {
        onView(withText(TITLE_GUARDIANS)).perform(click())

        intended(allOf(hasAction(Intent.ACTION_VIEW), hasData(LINK_GUARDIANS.toUri())))
    }

    @Test
    fun swiping_webmark_right_archives_it() {
        onView(withText(TITLE_GUARDIANS)).perform(swipeRight())

        onView(withText(TITLE_GUARDIANS)).check(doesNotExist())
        val guardiansWebmark = queries.selectRead(TITLE_GUARDIANS).executeAsOne()
        assertNotNull("Webmark should be found as archived", guardiansWebmark)

        onView(withId(R.id.archiveToggleButton)).perform(click())

        onView(withText(TITLE_GUARDIANS)).check(matches(isDisplayed()))
    }

    @Test
    fun swiping_archived_webmark_left_unarchives_it() {
        onView(withId(R.id.archiveToggleButton)).perform(click())
        onView(withText(TITLE_ENDGAME_PRESELLS)).perform(swipeLeft())

        onView(withText(TITLE_ENDGAME_PRESELLS)).check(doesNotExist())

        val guardiansWebmark = queries.selectUnread(TITLE_ENDGAME_PRESELLS).executeAsOne()
        assertNotNull("Webmark should be found as unarchived", guardiansWebmark)

        onView(withId(R.id.archiveToggleButton)).perform(click())

        onView(withText(TITLE_ENDGAME_PRESELLS)).check(matches(isDisplayed()))
    }

    @Test
    fun swiping_archived_webmark_right_deletes_it() {
        onView(withId(R.id.archiveToggleButton)).perform(click())
        onView(withText(TITLE_ENDGAME_PRESELLS)).perform(swipeRight())

        onView(withText(TITLE_ENDGAME_PRESELLS)).check(doesNotExist())

        onView(withId(R.id.archiveToggleButton)).perform(click())

        onView(withText(TITLE_ENDGAME_PRESELLS)).check(doesNotExist())

        val allWebmarks = queries.selectAll().executeAsList()
        val guardiansWebmark = allWebmarks.single { it.title == TITLE_ENDGAME_PRESELLS }
        assertTrue("Webmark should be marked for deletion", guardiansWebmark.markedForDeletion)
    }

    @Test
    fun archive_action_displays_snackbar_with_undo_option() {
        onView(withText(TITLE_GUARDIANS)).perform(swipeRight())

        onView(withText(TITLE_GUARDIANS)).check(doesNotExist())
        onView(withText(R.string.info_archived)).check(matches(isDisplayed()))

        onView(withText(R.string.action_undo)).perform(click())

        onView(withText(TITLE_GUARDIANS)).check(matches(isDisplayed()))
    }

    @Test
    fun unarchive_action_displays_snackbar_with_undo_option() {
        onView(withId(R.id.archiveToggleButton)).perform(click())
        onView(withText(TITLE_ENDGAME_PRESELLS)).perform(swipeLeft())

        onView(withText(TITLE_ENDGAME_PRESELLS)).check(doesNotExist())
        onView(withText(R.string.info_unarchived)).check(matches(isDisplayed()))

        onView(withText(R.string.action_undo)).perform(click())

        onView(withText(TITLE_ENDGAME_PRESELLS)).check(matches(isDisplayed()))
    }

    @Test
    fun delete_action_displays_snackbar_with_undo_option() {
        onView(withId(R.id.archiveToggleButton)).perform(click())
        onView(withText(TITLE_ENDGAME_PRESELLS)).perform(swipeRight())

        onView(withText(TITLE_ENDGAME_PRESELLS)).check(doesNotExist())
        onView(withText(R.string.info_deleted)).check(matches(isDisplayed()))

        onView(withText(R.string.action_undo)).perform(click())

        onView(withText(TITLE_ENDGAME_PRESELLS)).check(matches(isDisplayed()))
    }
}

