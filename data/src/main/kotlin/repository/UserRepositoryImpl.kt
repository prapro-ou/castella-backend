package repository.repository

import DomainException
import com.vb4.result.ApiResult
import destination.Destination
import destination.DestinationId
import destination.DestinationName
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.JoinType
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import repository.runCatchWithTransaction
import repository.table.AvatarsTable
import repository.table.DMsAvatarsTable
import repository.table.DMsTable
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
                .innerJoin(DMsTable)
                .innerJoin(DMsAvatarsTable)
                .innerJoin(AvatarsTable)
                .select { UsersTable.email eq email.value }
                .toList()
                .toUser()
        }
}

private fun List<ResultRow>.toUser() = User(
    email = Email(""),
    destinations = listOf(),
)