package me.thanel.webmark.list

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.hasDescendant
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import me.thanel.webmark.R
import me.thanel.webmark.core.base.SampleDataTest
import me.thanel.webmark.core.data.TITLE_ARCHIVED_WEBMARK
import me.thanel.webmark.core.data.TITLE_BLACK_PANTHER
import me.thanel.webmark.core.data.TITLE_CAPTAIN_MARVEL
import me.thanel.webmark.core.data.TITLE_WEBMARK
import me.thanel.webmark.core.matcher.hasItemCount
import me.thanel.webmark.core.matcher.withRecyclerViewItem
import org.junit.Test

class WebmarkVisibilityTest : SampleDataTest() {

    @Test
    fun displays_saved_webmarks() {
        onView(withId(R.id.webmarkRecyclerView))
            .check(matches(hasItemCount(3)))

        onView(withRecyclerViewItem(R.id.webmarkRecyclerView, 0))
            .check(matches(hasDescendant(withText(TITLE_WEBMARK))))
            .check(matches(hasDescendant(withText("theandrewblog.net ⸱ 12 min"))))

        onView(withRecyclerViewItem(R.id.webmarkRecyclerView, 1))
            .check(matches(hasDescendant(withText(TITLE_BLACK_PANTHER))))
            .check(matches(hasDescendant(withText("theandrewblog.net"))))

        onView(withRecyclerViewItem(R.id.webmarkRecyclerView, 2))
            .check(matches(hasDescendant(withText(TITLE_CAPTAIN_MARVEL))))
            .check(matches(hasDescendant(withText("theandrewblog.net ⸱ 360 min"))))
    }

    @Test
    fun displays_archived_webmarks() {
        onView(withId(R.id.archiveToggleButton)).perform(click())

        onView(withId(R.id.webmarkRecyclerView))
            .check(matches(hasItemCount(1)))

        onView(withRecyclerViewItem(R.id.webmarkRecyclerView, 0))
            .check(matches(hasDescendant(withText(TITLE_ARCHIVED_WEBMARK))))
    }
}

