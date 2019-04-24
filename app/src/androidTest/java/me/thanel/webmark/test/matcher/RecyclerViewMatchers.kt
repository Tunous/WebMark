package me.thanel.webmark.test.matcher

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.matcher.BoundedMatcher
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.TypeSafeMatcher

fun hasItemCount(itemCount: Int) =
    object : BoundedMatcher<View, RecyclerView>(RecyclerView::class.java) {
        override fun matchesSafely(item: RecyclerView): Boolean {
            return item.adapter?.itemCount == itemCount
        }

        override fun describeTo(description: Description) {
            description.appendText("has item count: ").appendValue(itemCount)
        }
    }

fun withRecyclerViewItem(recyclerViewId: Int, position: Int): Matcher<View> =
    object : TypeSafeMatcher<View>() {
        override fun matchesSafely(item: View): Boolean {
            val recyclerView = item.rootView.findViewById<RecyclerView>(recyclerViewId)
            if (recyclerView == null || recyclerView.id != recyclerViewId) return false
            val itemView =
                recyclerView.findViewHolderForAdapterPosition(position)?.itemView ?: return false
            return item === itemView
        }

        override fun describeTo(description: Description) {
            description.appendText("with recycler view ($recyclerViewId) child at position: $position")
        }
    }
