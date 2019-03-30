package me.thanel.webmark.ext

import com.squareup.sqldelight.Transacter
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Job
import kotlin.coroutines.coroutineContext

suspend fun <T> Transacter.transactionWithResult(body: Transacter.Transaction.() -> T): T? {
    val deferred = CompletableDeferred<T?>(parent = coroutineContext[Job])
    transaction {
        val result = body()

        afterCommit {
            deferred.complete(result)
        }

        afterRollback {
            deferred.complete(null)
        }
    }
    return deferred.await()
}
