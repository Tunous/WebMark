package me.thanel.webmark.core.matcher

import android.view.View
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.ViewInteraction
import androidx.test.espresso.matcher.RootMatchers.isPlatformPopup
import org.hamcrest.Matcher

fun onViewInPopup(viewMatcher: Matcher<View>): ViewInteraction =
    onView(viewMatcher).inRoot(isPlatformPopup())
