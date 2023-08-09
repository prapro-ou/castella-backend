package com.vb4.repository

import com.vb4.DomainException
import com.vb4.dm.DM
import com.vb4.dm.DMId
import com.vb4.dm.DMRepository
import com.vb4.result.ApiResult
import db.table.AvatarsTable
import db.table.DMsAvatarsTable
import db.table.DMsTable
import db.table.toDM
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.select
import repository.com.vb4.runCatchWithTransaction

class DMRepositoryImpl(
    private val database: Database,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
) : DMRepository {
    override suspend fun getDM(
        dmId: DMId,
    ): ApiResult<DM, DomainException> = runCatchWithTransaction(database, dispatcher) {
        DMsTable
            .innerJoin(DMsAvatarsTable)
            .innerJoin(AvatarsTable)
            .select { DMsTable.id eq dmId.value }
            .first()
            .toDM()
    }
}