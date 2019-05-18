package me.thanel.webmark.ui.list

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.hasDescendant
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import me.thanel.webmark.R
import me.thanel.webmark.test.base.ui.SampleDataUserInterfaceTest
import me.thanel.webmark.test.data.TITLE_ARCHIVED_WEBMARK
import me.thanel.webmark.test.data.TITLE_BLACK_PANTHER
import me.thanel.webmark.test.data.TITLE_CAPTAIN_MARVEL
import me.thanel.webmark.test.data.TITLE_WEBMARK
import me.thanel.webmark.test.matcher.hasItemCount
import me.thanel.webmark.test.matcher.withRecyclerViewItem
import org.hamcrest.CoreMatchers.allOf
import org.hamcrest.CoreMatchers.not
import org.junit.Test

class WebMarkVisibilityTest : SampleDataUserInterfaceTest() {

    @Test
    fun displays_saved_webmarks() {
        drain()
        onView(withId(R.id.webmarkRecyclerView))
            .check(matches(hasItemCount(4)))

        onView(withRecyclerViewItem(R.id.webmarkRecyclerView, 0))
            .check(matches(hasDescendant(withText(TITLE_WEBMARK))))
            .check(matches(hasDescendant(withText("theandrewblog.net ⸱ 12 min"))))
            .check(matches(hasDescendant(allOf(withId(R.id.webmarkImageView), isDisplayed()))))
            .check(matches(hasDescendant(allOf(withId(R.id.webmarkFaviconView), not(isDisplayed())))))

        onView(withRecyclerViewItem(R.id.webmarkRecyclerView, 1))
            .check(matches(hasDescendant(withText(TITLE_BLACK_PANTHER))))
            .check(matches(hasDescendant(withText("theandrewblog.net"))))
            .check(matches(hasDescendant(allOf(withId(R.id.webmarkImageView), not(isDisplayed())))))
            .check(matches(hasDescendant(allOf(withId(R.id.webmarkFaviconView), isDisplayed()))))

        onView(withRecyclerViewItem(R.id.webmarkRecyclerView, 2))
            .check(matches(hasDescendant(withText(TITLE_CAPTAIN_MARVEL))))
            .check(matches(hasDescendant(withText("theandrewblog.net ⸱ 360 min"))))
            .check(matches(hasDescendant(allOf(withId(R.id.webmarkImageView), isDisplayed()))))
            .check(matches(hasDescendant(allOf(withId(R.id.webmarkFaviconView), isDisplayed()))))
    }

    @Test
    fun displays_archived_webmarks() {
        onView(withId(R.id.archiveToggleButton)).perform(click())
        drain()

        onView(withId(R.id.webmarkRecyclerView))
            .check(matches(hasItemCount(1)))

        onView(withRecyclerViewItem(R.id.webmarkRecyclerView, 0))
            .check(matches(hasDescendant(withText(TITLE_ARCHIVED_WEBMARK))))
    }
}
