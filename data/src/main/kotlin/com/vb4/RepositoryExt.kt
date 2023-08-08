package repository.com.vb4

import com.vb4.DomainException
import com.vb4.result.ApiResult
import kotlin.coroutines.cancellation.CancellationException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.Transaction
import org.jetbrains.exposed.sql.transactions.transaction

suspend inline fun <T> runCatchWithTransaction(
    database: Database,
    dispatcher: CoroutineDispatcher,
    noinline block: Transaction.() -> T,
): ApiResult<T, DomainException> = try {
    withContext(dispatcher) {
        transaction(database, block)
    }.let { ApiResult.Success(it) }
} catch (exception: Exception) {
    when (exception) {
        is NoSuchElementException ->
            ApiResult.Failure(
                DomainException.NoSuchElementException(exception.message ?: ""),
            )
        else ->
            ApiResult.Failure(DomainException.SystemException(exception.message.orEmpty(), exception))
    }
}

inline fun <T> runCatchDomainException(block: () -> T): ApiResult<T, DomainException> = try {
    ApiResult.Success(block())
} catch (e: CancellationException) {
    throw e
} catch (e: Exception) {
    ApiResult.Failure(DomainException.SystemException(e.message.orEmpty(), e))
}
