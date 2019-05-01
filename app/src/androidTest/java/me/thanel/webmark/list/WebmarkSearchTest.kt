package me.thanel.webmark.list

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.doesNotExist
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import me.thanel.webmark.R
import me.thanel.webmark.test.base.SampleDataTest
import me.thanel.webmark.test.data.TITLE_ARCHIVED_WEBMARK
import me.thanel.webmark.test.data.TITLE_BLACK_PANTHER
import me.thanel.webmark.test.data.TITLE_CAPTAIN_MARVEL
import me.thanel.webmark.test.data.TITLE_ENDGAME_TRAILER
import me.thanel.webmark.test.data.TITLE_WEBMARK
import org.hamcrest.CoreMatchers
import org.hamcrest.core.AllOf
import org.junit.Test

class WebmarkSearchTest : SampleDataTest() {

    @Test
    fun pressing_on_search_toggle_toggles_visibility_and_focus_of_search_input() {
        onView(withId(R.id.searchInputView)).check(matches(CoreMatchers.not(ViewMatchers.hasFocus())))

        onView(withId(R.id.searchToggleButton)).perform(click())

        onView(withId(R.id.searchInputView)).check(matches(AllOf.allOf(isDisplayed(), ViewMatchers.hasFocus())))

        onView(withId(R.id.searchToggleButton)).perform(click())

        onView(withId(R.id.searchInputView)).check(matches(CoreMatchers.not(ViewMatchers.hasFocus())))
    }

    @Test
    fun it_is_possible_to_toggle_archive_when_search_input_is_visible() {
        onView(withId(R.id.searchToggleButton)).perform(click())

        onView(withId(R.id.archiveToggleButton)).check(matches(AllOf.allOf(isDisplayed(), ViewMatchers.isNotChecked())))
        onView(withId(R.id.archiveToggleButton)).perform(click())

        onView(withId(R.id.archiveToggleButton)).check(matches(ViewMatchers.isChecked()))
    }

    @Test
    fun displays_empty_view_when_there_are_no_matches() {
        onView(withId(R.id.searchToggleButton)).perform(click())
        onView(withId(R.id.searchInputView)).perform(typeText("blabla"))

        onView(withText(R.string.title_nothing_found)).check(matches(isDisplayed()))
        onView(withText(R.string.info_save_instructions)).check(matches(isDisplayed()))
    }

    @Test
    fun displays_results_from_archive_only_when_viewing_archived_webmarks() {
        onView(withId(R.id.searchToggleButton)).perform(click())
        onView(withId(R.id.searchInputView)).perform(typeText("Presells"))

        onView(withText(TITLE_ARCHIVED_WEBMARK)).check(doesNotExist())
        onView(withText(R.string.title_nothing_found)).check(matches(isDisplayed()))

        onView(withId(R.id.archiveToggleButton)).perform(click())

        onView(withText(TITLE_ARCHIVED_WEBMARK)).check(matches(isDisplayed()))
    }

    @Test
    fun searches_webmark_via_title() {
        onView(withText(TITLE_BLACK_PANTHER)).check(matches(isDisplayed()))
        onView(withText(TITLE_CAPTAIN_MARVEL)).check(matches(isDisplayed()))

        onView(withId(R.id.searchToggleButton)).perform(click())
        onView(withId(R.id.searchInputView)).perform(typeText("Black Panther"))

        onView(withText(TITLE_BLACK_PANTHER)).check(matches(isDisplayed()))
        onView(withText(TITLE_CAPTAIN_MARVEL)).check(doesNotExist())
    }

    @Test
    fun searches_webmark_via_link() {
        onView(withText(TITLE_ENDGAME_TRAILER)).check(matches(isDisplayed()))

        onView(withId(R.id.searchToggleButton)).perform(click())
        onView(withId(R.id.searchInputView)).perform(typeText("theandrewblog.net"))

        onView(withText(TITLE_ENDGAME_TRAILER)).check(doesNotExist())
        onView(withText(TITLE_WEBMARK)).check(matches(isDisplayed()))
    }

    @Test
    fun closing_search_resets_list() {
        onView(withId(R.id.searchToggleButton)).perform(click())
        onView(withId(R.id.searchInputView)).perform(typeText("Black Panther"))

        onView(withText(TITLE_CAPTAIN_MARVEL)).check(doesNotExist())

        onView(withId(R.id.searchToggleButton)).perform(click())

        onView(withText(TITLE_CAPTAIN_MARVEL)).check(matches(isDisplayed()))
    }
}
