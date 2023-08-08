package com.vb4.repository

import com.vb4.DomainException
import com.vb4.destination.Destination
import com.vb4.destination.DestinationId
import com.vb4.destination.DestinationRepository
import com.vb4.result.ApiResult
import db.table.AvatarsTable
import db.table.DMsAvatarsTable
import db.table.DMsTable
import db.table.GroupsAvatarsTable
import db.table.GroupsTable
import db.table.UsersTable
import db.table.toDM
import db.table.toGroup
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.select
import repository.com.vb4.runCatchWithTransaction

class DestinationRepositoryImpl(
    private val database: Database,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
) : DestinationRepository {
    override suspend fun getDestination(
        destinationId: DestinationId,
    ): ApiResult<Destination, DomainException> = runCatchWithTransaction(database, dispatcher) {
        val dm = DMsTable
            .innerJoin(DMsAvatarsTable)
            .innerJoin(AvatarsTable)
            .select { DMsTable.id eq destinationId.value }
            .firstOrNull()
            ?.toDM()

        val group = GroupsTable
            .innerJoin(GroupsAvatarsTable)
            .innerJoin(AvatarsTable)
            .select { GroupsTable.id eq destinationId.value }
            .toList()

        dm ?: if(group.isNotEmpty()) group.toGroup() else throw DomainException.NoSuchElementException("")

    }
}