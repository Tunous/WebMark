package me.thanel.webmark

import androidx.annotation.VisibleForTesting
import androidx.test.espresso.idling.CountingIdlingResource

object EspressoIdlingResource {
    @VisibleForTesting
    var idlingResource: CountingIdlingResource? = null

    fun increment() {
        idlingResource?.increment()
    }

    fun decrement() {
        idlingResource?.decrement()
    }
}
