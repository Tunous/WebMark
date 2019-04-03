package me.thanel.webmark.ext

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Transformations

fun <X, Y> LiveData<X>.map(mapFunction: (X) -> Y) = Transformations.map(this, mapFunction)

fun <X, Y> LiveData<X>.switchMap(switchMapFunction: (X) -> LiveData<Y>) =
    Transformations.switchMap(this, switchMapFunction)

fun <X, Y> LiveData<X>.combineWith(other: LiveData<Y>): LiveData<Pair<X, Y>> {
    val liveData = MediatorLiveData<Pair<X, Y>>()

    var latestA: X? = null
    var latestB: Y? = null

    fun notify() {
        val currentA = latestA ?: return
        val currentB = latestB ?: return
        liveData.value = currentA to currentB
    }

    liveData.addSource(this) {
        latestA = it
        notify()
    }
    liveData.addSource(other) {
        latestB = it
        notify()
    }
    return liveData
}
