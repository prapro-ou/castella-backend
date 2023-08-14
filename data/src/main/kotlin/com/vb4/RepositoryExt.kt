package com.vb4

import com.vb4.result.ApiResult
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.Transaction
import org.jetbrains.exposed.sql.transactions.experimental.suspendedTransactionAsync
import org.jetbrains.exposed.sql.transactions.transactionManager
import kotlin.coroutines.cancellation.CancellationException

inline fun <T> runCatchDomainException(block: () -> T): ApiResult<T, DomainException> = try {
    ApiResult.Success(block())
} catch (e: CancellationException) {
    throw e
} catch (e: NoSuchElementException) {
    ApiResult.Failure(DomainException.NoSuchElementException(e.message.orEmpty()))
} catch (e: DomainException) {
    ApiResult.Failure(e)
} catch (e: Exception) {
    ApiResult.Failure(DomainException.SystemException(e.message.orEmpty(), e))
}

suspend fun <T> runCatchWithContext(
    dispatcher: CoroutineDispatcher,
    block: suspend CoroutineScope.() -> T,
): ApiResult<T, DomainException> = withContext(dispatcher) { runCatchDomainException { block() } }

suspend fun <T> suspendTransaction(
    db: Database,
    statement: suspend Transaction.() -> T,
) = suspendTransactionAsync(db, statement).await()

suspend fun <T> suspendTransactionAsync(
    db: Database,
    statement: suspend Transaction.() -> T,
) = coroutineScope {
    suspendedTransactionAsync(
        context = coroutineContext,
        db = db,
        transactionIsolation = db.transactionManager.defaultIsolationLevel,
        statement = statement,
    )
}
