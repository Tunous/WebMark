package me.thanel.webmark

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withText
import org.junit.Test

class WebmarkListTest : BaseTest() {
    @Test
    fun initiallyApplicationDisplaysEmptyViewWithInstructions() {
        onView(withText(R.string.title_nothing_saved)).check(matches(isDisplayed()))
        onView(withText(R.string.info_save_instructions)).check(matches(isDisplayed()))
    }
}
