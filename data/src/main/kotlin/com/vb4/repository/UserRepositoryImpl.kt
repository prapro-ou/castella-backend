package com.vb4.repository

import com.vb4.DomainException
import com.vb4.avatar.Avatar
import com.vb4.result.ApiResult
import com.vb4.destination.Destination
import com.vb4.destination.DestinationId
import com.vb4.destination.DestinationName
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.select
import repository.com.vb4.runCatchWithTransaction
import db.table.DMsTable
import db.table.GroupsTable
import db.table.UsersTable
import com.vb4.user.Email
import com.vb4.user.User
import com.vb4.user.UserRepository

class UserRepositoryImpl(
    private val database: Database,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
) : UserRepository {
    override suspend fun getUser(email: Email): ApiResult<User, DomainException> =
        runCatchWithTransaction(database, dispatcher) {
            val user = UsersTable
                .select { UsersTable.email eq email.value }
                .first()

            val dm = DMsTable.select { DMsTable.userEmail eq user[UsersTable.email] }.toList()
            val group = GroupsTable.select { GroupsTable.userEmail eq user[UsersTable.email] }.toList()

            user.toUser(dm.map { it.toDM() }, group.map { it.toGroup() })
        }
}

private fun ResultRow.toUser(dms: List<Destination.DM>, groups: List<Destination.Group>) = User(
    email = Email(this[UsersTable.email]),
    dms = dms,
    groups = groups
)

private fun ResultRow.toDM() = Destination.DM(
    id = DestinationId(this[DMsTable.id]),
    name = DestinationName(this[DMsTable.name]),
    to = Avatar(Email("")),
)

private fun ResultRow.toGroup() = Destination.Group(
    id = DestinationId(this[GroupsTable.id]),
    name = DestinationName(this[GroupsTable.name]),
    to = listOf(),
)
