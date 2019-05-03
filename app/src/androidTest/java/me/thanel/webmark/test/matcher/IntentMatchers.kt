package me.thanel.webmark.test.matcher

import android.app.Activity
import android.app.Instrumentation.ActivityResult
import android.content.ComponentName
import android.content.Intent
import androidx.core.net.toUri
import androidx.test.espresso.intent.Intents.intending
import androidx.test.espresso.intent.matcher.IntentMatchers.hasAction
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.intent.matcher.IntentMatchers.hasData
import androidx.test.espresso.intent.matcher.IntentMatchers.hasExtra
import androidx.test.espresso.intent.matcher.IntentMatchers.hasType
import androidx.test.espresso.intent.matcher.IntentMatchers.isInternal
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.allOf
import org.hamcrest.CoreMatchers.not
import org.hamcrest.Matcher

fun stubExternalIntents() {
    intending(not(isInternal())).respondWith(ActivityResult(Activity.RESULT_OK, null))
}

fun chooserIntent(intentMatcher: Matcher<Intent>): Matcher<Intent> = allOf(
    hasAction(Intent.ACTION_CHOOSER),
    hasExtra(`is`(Intent.EXTRA_INTENT), intentMatcher)
)

fun shareIntent(sharedText: String): Matcher<Intent> = allOf(
    hasAction(Intent.ACTION_SEND),
    hasType("text/plain"),
    hasExtra(Intent.EXTRA_TEXT, sharedText)
)

fun directShareIntent(component: ComponentName): Matcher<Intent> = allOf(
    hasAction(Intent.ACTION_SEND),
    hasType("text/plain"),
    hasComponent(component)
)

fun browserIntent(url: String): Matcher<Intent> = allOf(
    hasAction(Intent.ACTION_VIEW),
    hasData(url.toUri())
)
