package me.thanel.webmark.ui.list

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.doesNotExist
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.hasFocus
import androidx.test.espresso.matcher.ViewMatchers.isChecked
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.isNotChecked
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import me.thanel.webmark.R
import me.thanel.webmark.test.base.ui.SampleDataUserInterfaceTest
import me.thanel.webmark.test.data.TITLE_ARCHIVED_WEBMARK
import me.thanel.webmark.test.data.TITLE_BLACK_PANTHER
import me.thanel.webmark.test.data.TITLE_CAPTAIN_MARVEL
import me.thanel.webmark.test.data.TITLE_ENDGAME_TRAILER
import me.thanel.webmark.test.data.TITLE_WEBMARK
import org.hamcrest.CoreMatchers.allOf
import org.hamcrest.CoreMatchers.not
import org.junit.Test

class WebmarkSearchTest : SampleDataUserInterfaceTest() {

    @Test
    fun pressing_on_search_toggle_toggles_visibility_and_focus_of_search_input() {
        onView(withId(R.id.searchInputView)).check(matches(not(hasFocus())))

        onView(withId(R.id.searchToggleButton)).perform(click())

        onView(withId(R.id.searchInputView)).check(matches(allOf(isDisplayed(), hasFocus())))

        onView(withId(R.id.searchToggleButton)).perform(click())

        onView(withId(R.id.searchInputView)).check(matches(not(hasFocus())))
    }

    @Test
    fun it_is_possible_to_toggle_archive_when_search_input_is_visible() {
        onView(withId(R.id.searchToggleButton)).perform(click())

        onView(withId(R.id.archiveToggleButton))
            .check(matches(allOf(isDisplayed(), isNotChecked())))
        onView(withId(R.id.archiveToggleButton)).perform(click())

        onView(withId(R.id.archiveToggleButton)).check(matches(isChecked()))
    }

    @Test
    fun displays_empty_view_when_there_are_no_matches() {
        onView(withId(R.id.searchToggleButton)).perform(click())
        onView(withId(R.id.searchInputView)).perform(typeText("blabla"))
        drain()

        onView(withText(R.string.title_nothing_found)).check(matches(isDisplayed()))
        onView(withText(R.string.info_save_instructions)).check(matches(isDisplayed()))
    }

    @Test
    fun displays_results_from_archive_only_when_viewing_archived_webmarks() {
        onView(withId(R.id.searchToggleButton)).perform(click())
        onView(withId(R.id.searchInputView)).perform(typeText("Presells"))
        drain()

        onView(withText(TITLE_ARCHIVED_WEBMARK)).check(doesNotExist())
        onView(withText(R.string.title_nothing_found)).check(matches(isDisplayed()))

        onView(withId(R.id.archiveToggleButton)).perform(click())
        drain()

        onView(withText(TITLE_ARCHIVED_WEBMARK)).check(matches(isDisplayed()))
    }

    @Test
    fun searches_webmark_via_title() {
        drain()
        onView(withText(TITLE_BLACK_PANTHER)).check(matches(isDisplayed()))
        onView(withText(TITLE_CAPTAIN_MARVEL)).check(matches(isDisplayed()))

        onView(withId(R.id.searchToggleButton)).perform(click())
        onView(withId(R.id.searchInputView)).perform(typeText("Black Panther"))
        drain()

        onView(withText(TITLE_BLACK_PANTHER)).check(matches(isDisplayed()))
        onView(withText(TITLE_CAPTAIN_MARVEL)).check(doesNotExist())
    }

    @Test
    fun searches_webmark_via_link() {
        drain()
        onView(withText(TITLE_ENDGAME_TRAILER)).check(matches(isDisplayed()))

        onView(withId(R.id.searchToggleButton)).perform(click())
        onView(withId(R.id.searchInputView)).perform(typeText("theandrewblog.net"))
        drain()

        onView(withText(TITLE_ENDGAME_TRAILER)).check(doesNotExist())
        onView(withText(TITLE_WEBMARK)).check(matches(isDisplayed()))
    }

    @Test
    fun closing_search_resets_list() {
        onView(withId(R.id.searchToggleButton)).perform(click())
        onView(withId(R.id.searchInputView)).perform(typeText("Black Panther"))
        drain()

        onView(withText(TITLE_CAPTAIN_MARVEL)).check(doesNotExist())

        onView(withId(R.id.searchToggleButton)).perform(click())
        drain()

        onView(withText(TITLE_CAPTAIN_MARVEL)).check(matches(isDisplayed()))
    }
}
