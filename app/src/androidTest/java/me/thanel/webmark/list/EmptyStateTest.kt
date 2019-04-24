package me.thanel.webmark.list

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isChecked
import androidx.test.espresso.matcher.ViewMatchers.isClickable
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.isNotChecked
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import me.thanel.webmark.R
import me.thanel.webmark.test.base.BaseTest
import me.thanel.webmark.preferences.WebMarkPreferences
import org.hamcrest.core.AllOf.allOf
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class EmptyStateTest : BaseTest() {

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

        onView(withText(R.string.title_nothing_archived)).check(matches(isDisplayed()))
        onView(withText(R.string.info_save_instructions)).check(matches(isDisplayed()))
    }

    @Test
    fun initially_all_toolbar_buttons_are_clickable() {
        onView(withId(R.id.searchToggleButton)).check(matches(allOf(isDisplayed(), isClickable())))
        onView(withId(R.id.archiveToggleButton)).check(matches(allOf(isDisplayed(), isClickable())))
        onView(withId(R.id.themeToggleButton)).check(matches(allOf(isDisplayed(), isClickable())))
    }

    @Test
    fun theme_toggle_works() {
        assertFalse("Theme should be set to light", WebMarkPreferences.useDarkTheme)
        onView(withId(R.id.themeToggleButton)).check(matches(isNotChecked()))

        onView(withId(R.id.themeToggleButton)).perform(click())

        assertTrue("Theme should change to dark", WebMarkPreferences.useDarkTheme)
        onView(withId(R.id.themeToggleButton)).check(matches(isChecked()))
    }

    @Test
    fun archive_toggle_changes_its_checked_state() {
        onView(withId(R.id.archiveToggleButton)).check(matches(isNotChecked()))

        onView(withId(R.id.archiveToggleButton)).perform(click())

        onView(withId(R.id.archiveToggleButton)).check(matches(isChecked()))
    }
}
