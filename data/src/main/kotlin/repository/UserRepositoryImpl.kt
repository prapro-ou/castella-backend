package repository.repository

import DomainException
import com.vb4.result.ApiResult
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.select
import repository.runCatchWithTransaction
import repository.table.UsersTable
import user.Email
import user.User
import user.UserRepository

class UserRepositoryImpl(
    private val database: Database,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
) : UserRepository {
    override suspend fun getUser(email: Email): ApiResult<User, DomainException> =
        runCatchWithTransaction(database, dispatcher) {
            UsersTable
                .select { UsersTable.email eq email.value }
                .toList()
                .toUser()
        }
}

private fun List<ResultRow>.toUser() = User(
    email = Email(this.joinToString { it.toString() }),
    destinations = listOf(),
)
