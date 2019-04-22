package me.thanel.webmark

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.hasDescendant
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import me.thanel.webmark.base.BaseTest
import me.thanel.webmark.data.Database
import me.thanel.webmark.data.TITLE_BLACK_PANTHER
import me.thanel.webmark.data.TITLE_CAPTAIN_MARVEL
import me.thanel.webmark.data.TITLE_ENDGAME_PRESELLS
import me.thanel.webmark.data.TITLE_GUARDIANS
import me.thanel.webmark.data.WebmarkQueries
import me.thanel.webmark.data.insertSampleData
import me.thanel.webmark.matcher.hasItemCount
import me.thanel.webmark.matcher.withRecyclerViewItem
import org.junit.Before
import org.junit.Test
import org.kodein.di.generic.instance

class WebmarkListVisibilityTest : BaseTest() {

    private lateinit var queries: WebmarkQueries

    @Before
    fun setupDatabase() {
        val database = dkodein.instance<Database>()
        queries = database.webmarkQueries
        queries.insertSampleData()
        startActivity()
    }

    @Test
    fun displays_saved_webmarks() {
        onView(withId(R.id.webmarkRecyclerView))
            .check(matches(hasItemCount(3)))

        onView(withRecyclerViewItem(R.id.webmarkRecyclerView, 0))
            .check(matches(hasDescendant(withText(TITLE_GUARDIANS))))
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
            .check(matches(hasDescendant(withText(TITLE_ENDGAME_PRESELLS))))
    }
}

