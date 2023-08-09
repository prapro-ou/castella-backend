package com.vb4.repository

import com.vb4.DomainException
import com.vb4.Email
import com.vb4.result.ApiResult
import com.vb4.user.User
import com.vb4.user.UserRepository
import db.table.AvatarsTable
import db.table.DMsAvatarsTable
import db.table.DMsTable
import db.table.GroupsAvatarsTable
import db.table.GroupsTable
import db.table.UsersTable
import db.table.toDM
import db.table.toGroup
import db.table.toUser
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.select
import repository.com.vb4.runCatchWithTransaction

class UserRepositoryImpl(
    private val database: Database,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
) : UserRepository {
    override suspend fun getUser(email: Email): ApiResult<User, DomainException> =
        runCatchWithTransaction(database, dispatcher) {
            val user = UsersTable
                .select { UsersTable.email eq email.value }
                .first()

            val dms = DMsTable
                .innerJoin(DMsAvatarsTable)
                .innerJoin(AvatarsTable)
                .select { DMsTable.userEmail eq user[UsersTable.email] }
                .map { it.toDM() }

            val groups = GroupsTable
                .innerJoin(GroupsAvatarsTable)
                .innerJoin(AvatarsTable)
                .select { GroupsTable.userEmail eq user[UsersTable.email] }
                .groupBy(keySelector = { it[GroupsTable.id] })
                .map { (_, value) -> value.toGroup() }

            user.toUser(dms, groups)
        }
}
