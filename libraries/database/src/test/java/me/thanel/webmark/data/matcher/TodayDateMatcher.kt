package me.thanel.webmark.data.matcher

import org.hamcrest.Description
import org.hamcrest.TypeSafeMatcher
import java.util.Calendar
import java.util.Date

class TodayDateMatcher : TypeSafeMatcher<Date>() {
    override fun describeTo(description: Description) {
        description.appendText("is today date ").appendValue(Date())
    }

    override fun matchesSafely(item: Date): Boolean {
        val calendar = Calendar.getInstance()
        calendar.time = item

        val thenYear = calendar.get(Calendar.YEAR)
        val thenMonth = calendar.get(Calendar.MONTH)
        val thenMonthDay = calendar.get(Calendar.DAY_OF_MONTH)

        calendar.time = Date()
        return (thenYear == calendar.get(Calendar.YEAR) &&
            thenMonth == calendar.get(Calendar.MONTH) &&
            thenMonthDay == calendar.get(Calendar.DAY_OF_MONTH))
    }
}

fun isToday() = TodayDateMatcher()
