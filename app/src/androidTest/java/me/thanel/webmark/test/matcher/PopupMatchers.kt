package me.thanel.webmark.test.matcher

import android.app.Activity
import android.view.View
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.ViewInteraction
import androidx.test.espresso.matcher.RootMatchers.isPlatformPopup
import androidx.test.espresso.matcher.RootMatchers.withDecorView
import androidx.test.rule.ActivityTestRule
import org.hamcrest.CoreMatchers.not
import org.hamcrest.Matcher

fun onViewInPopup(viewMatcher: Matcher<View>): ViewInteraction =
    onView(viewMatcher).inRoot(isPlatformPopup())

fun <T : Activity> ActivityTestRule<T>.onTooltipView(viewMatcher: Matcher<View>): ViewInteraction =
    onView(viewMatcher).inRoot(withDecorView(not(activity.window.decorView)))
