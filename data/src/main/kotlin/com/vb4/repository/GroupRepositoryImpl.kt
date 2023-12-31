package com.vb4.repository

import com.vb4.DomainException
import com.vb4.Email
import com.vb4.NewMessageCount
import com.vb4.group.Group
import com.vb4.group.GroupId
import com.vb4.group.GroupRepository
import com.vb4.result.ApiResult
import com.vb4.runCatchWithContext
import com.vb4.suspendTransaction
import db.table.AvatarsTable
import db.table.GroupMessagesTable
import db.table.GroupsAvatarsTable
import db.table.GroupsTable
import db.table.toGroup
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.insertIgnore
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.UUID

class GroupRepositoryImpl(
    private val database: Database,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
) : GroupRepository {
    override suspend fun getGroupsByUserEmail(userEmail: Email): ApiResult<List<Group>, DomainException> =
        runCatchWithContext(dispatcher) {
            suspendTransaction(database) {
                GroupsTable
                    .innerJoin(GroupsAvatarsTable)
                    .innerJoin(AvatarsTable)
                    .select { GroupsTable.userEmail eq userEmail.value }
                    .groupBy(keySelector = { it[GroupsTable.id] })
            }.map { (groupId, groups) ->
                groups.toGroup(newMessageCount = getNewMessageCount(GroupId(groupId)))
            }
        }

    override suspend fun getGroup(groupId: GroupId): ApiResult<Group, DomainException> =
        runCatchWithContext(dispatcher) {
            val group = suspendTransaction(database) {
                GroupsTable
                    .innerJoin(GroupsAvatarsTable)
                    .innerJoin(AvatarsTable)
                    .select { GroupsTable.id eq groupId.value }
                    .toList()
            }
            group.toGroup(newMessageCount = getNewMessageCount(GroupId(group.first()[GroupsTable.id])))
        }

    override suspend fun insertGroup(group: Group): ApiResult<Unit, DomainException> =
        runCatchWithContext(dispatcher) {
            suspendTransaction(database) {
                GroupsTable
                    .insert {
                        it[id] = group.id.value
                        it[name] = group.name.value
                        it[userEmail] = group.userEmail.value
                    }
                group.to
                    .map { it.email }
                    .forEach { email ->
                        AvatarsTable
                            .insertIgnore { it[AvatarsTable.email] = email.value }

                        GroupsAvatarsTable
                            .insert {
                                it[id] = UUID.randomUUID().toString()
                                it[groupId] = group.id.value
                                it[avatarEmail] = email.value
                            }
                    }
            }
        }

    private fun getNewMessageCount(groupId: GroupId) = NewMessageCount(
        transaction(database) {
            GroupMessagesTable
                .select { GroupMessagesTable.id eq groupId.value }
                .count { it[GroupMessagesTable.isRecent] }
        },
    )
}
