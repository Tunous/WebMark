package me.thanel.webmark.ui.list

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.longClick
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isChecked
import androidx.test.espresso.matcher.ViewMatchers.isClickable
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.isNotChecked
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import me.thanel.webmark.R
import me.thanel.webmark.test.base.ui.BaseUserInterfaceTest
import me.thanel.webmark.test.matcher.onTooltipView
import org.hamcrest.core.AllOf.allOf
import org.junit.Before
import org.junit.Test

class EmptyStateTest : BaseUserInterfaceTest() {

    @Before
    override fun setup() {
        super.setup()
        startActivity()
    }

    @Test
    fun initially_app_displays_info_view() {
        onView(withText(R.string.title_nothing_saved)).check(matches(isDisplayed()))
        onView(withText(R.string.info_save_instructions)).check(matches(isDisplayed()))
    }

    @Test
    fun going_to_archive_changes_title_of_info_view() {
        onView(withId(R.id.archiveToggleButton)).perform(click())
        drain()

        onView(withText(R.string.title_nothing_archived)).check(matches(isDisplayed()))
        onView(withText(R.string.info_save_instructions)).check(matches(isDisplayed()))
    }

    @Test
    fun initially_all_toolbar_buttons_are_clickable() {
        onView(withId(R.id.searchToggleButton)).check(matches(allOf(isDisplayed(), isClickable())))
        onView(withId(R.id.archiveToggleButton)).check(matches(allOf(isDisplayed(), isClickable())))
        onView(withId(R.id.moreOptionsButton)).check(matches(allOf(isDisplayed(), isClickable())))
    }

    @Test
    fun archive_toggle_changes_its_checked_state() {
        onView(withId(R.id.archiveToggleButton)).check(matches(isNotChecked()))

        onView(withId(R.id.archiveToggleButton)).perform(click())

        onView(withId(R.id.archiveToggleButton)).check(matches(isChecked()))
    }

    @Test
    fun search_toggle_button_shows_tooltip() {
        onView(withId(R.id.searchToggleButton)).perform(longClick())

        activityRule.onTooltipView(withText(R.string.action_search)).check(matches(isDisplayed()))

        onView(withId(R.id.searchToggleButton)).perform(click()).perform(longClick())

        activityRule.onTooltipView(withText(R.string.action_close_search))
            .check(matches(isDisplayed()))
    }

    @Test
    fun archive_toggle_button_shows_tooltip() {
        onView(withId(R.id.archiveToggleButton)).perform(longClick())

        activityRule.onTooltipView(withText(R.string.action_show_archive))
            .check(matches(isDisplayed()))

        onView(withId(R.id.archiveToggleButton)).perform(click()).perform(longClick())

        activityRule.onTooltipView(withText(R.string.action_hide_archive))
            .check(matches(isDisplayed()))
    }
}
