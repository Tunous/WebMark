package me.thanel.webmark.data.ext

import androidx.annotation.CheckResult
import androidx.annotation.MainThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.squareup.sqldelight.Query
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

@CheckResult
fun <T : Any> Query<T>.asLiveData(): LiveData<Query<T>> =
    QueryListenerLiveData(this)

private class QueryListenerLiveData<T : Any>(
    private val query: Query<T>
) : LiveData<Query<T>>(), Query.Listener {

    override fun onActive() {
        query.addListener(this)
        postValue(query)
    }

    override fun queryResultsChanged() {
        postValue(query)
    }

    override fun onInactive() {
        query.removeListener(this)
    }
}

@CheckResult
fun <T : Any> LiveData<Query<T>>.mapToOne(scope: CoroutineScope): LiveData<T> = map(scope) {
    it.executeAsOne()
}

@MainThread
@CheckResult
fun <T : Any> LiveData<Query<T>>.mapToOneOrDefault(
    scope: CoroutineScope,
    defaultValue: T
): LiveData<T> = map(scope) {
    it.executeAsOneOrNull() ?: defaultValue
}

@MainThread
@CheckResult
fun <T : Any> LiveData<Query<T>>.mapToList(scope: CoroutineScope): LiveData<List<T>> = map(scope) {
    it.executeAsList()
}

private fun <T, R> LiveData<T>.map(
    scope: CoroutineScope,
    context: CoroutineContext = Dispatchers.IO,
    block: (T) -> R
): LiveData<R> = MediatorLiveData<R>().also { liveData ->
    liveData.addSource(this) {
        scope.launch(context) {
            liveData.postValue(block(it))
        }
    }
}
