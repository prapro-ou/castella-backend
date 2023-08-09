package com.vb4.repository

import com.vb4.DomainException
import com.vb4.group.Group
import com.vb4.group.GroupId
import com.vb4.group.GroupRepository
import com.vb4.result.ApiResult
import db.table.AvatarsTable
import db.table.GroupsAvatarsTable
import db.table.GroupsTable
import db.table.toGroup
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.select
import repository.com.vb4.runCatchWithTransaction

class GroupRepositoryImpl(
    private val database: Database,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
) : GroupRepository {
    override suspend fun getGroup(groupId: GroupId): ApiResult<Group, DomainException> =
        runCatchWithTransaction(database, dispatcher) {
            GroupsTable
                .innerJoin(GroupsAvatarsTable)
                .innerJoin(AvatarsTable)
                .select { GroupsTable.id eq groupId.value }
                .toList()
                .toGroup()
        }
}
