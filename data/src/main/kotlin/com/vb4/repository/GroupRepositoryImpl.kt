package com.vb4.repository

import com.vb4.DomainException
import com.vb4.group.Group
import com.vb4.group.GroupId
import com.vb4.group.GroupMessage
import com.vb4.group.GroupMessageId
import com.vb4.group.GroupRepository
import com.vb4.result.ApiResult
import com.vb4.runCatchWithContext
import com.vb4.suspendTransaction
import db.table.AvatarsTable
import db.table.GroupsAvatarsTable
import db.table.GroupsTable
import db.table.toGroup
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction

class GroupRepositoryImpl(
    private val database: Database,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
) : GroupRepository {
    override suspend fun getGroup(groupId: GroupId): ApiResult<Group, DomainException> =
        runCatchWithContext(dispatcher) {
            suspendTransaction(database) {
                GroupsTable
                    .innerJoin(GroupsAvatarsTable)
                    .innerJoin(AvatarsTable)
                    .select { GroupsTable.id eq groupId.value }
                    .toList()
                    .toGroup()
            }
        }

    override suspend fun getGroupMessages(groupId: GroupId): ApiResult<List<GroupMessage>, DomainException> {
        TODO("Not yet implemented")
    }

    override suspend fun getGroupMessage(
        groupId: GroupId,
        messageId: GroupMessageId
    ): ApiResult<GroupMessage, DomainException> {
        TODO("Not yet implemented")
    }
}
