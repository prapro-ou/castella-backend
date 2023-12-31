package com.vb4.repository

import com.vb4.DomainException
import com.vb4.Email
import com.vb4.NewMessageCount
import com.vb4.dm.DM
import com.vb4.dm.DMId
import com.vb4.dm.DMRepository
import com.vb4.result.ApiResult
import com.vb4.runCatchWithContext
import com.vb4.suspendTransaction
import db.table.AvatarsTable
import db.table.DMMessagesTable
import db.table.DMsAvatarsTable
import db.table.DMsTable
import db.table.toDM
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.insertIgnore
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.UUID

class DMRepositoryImpl(
    private val database: Database,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
) : DMRepository {
    override suspend fun getDMsByUserEmail(userEmail: Email): ApiResult<List<DM>, DomainException> =
        runCatchWithContext(dispatcher) {
            suspendTransaction(database) {
                DMsTable
                    .innerJoin(DMsAvatarsTable)
                    .innerJoin(AvatarsTable)
                    .select { DMsTable.userEmail eq userEmail.value }
                    .toList()
            }.map { dm ->
                dm.toDM(
                    newMessageCount = getNewMessageCount(DMId(dm[DMsTable.id])),
                )
            }
        }

    override suspend fun getDM(
        dmId: DMId,
    ): ApiResult<DM, DomainException> = runCatchWithContext(dispatcher) {
        val dm = suspendTransaction(database) {
            DMsTable
                .innerJoin(DMsAvatarsTable)
                .innerJoin(AvatarsTable)
                .select { DMsTable.id eq dmId.value }
                .first()
        }
        dm.toDM(
            newMessageCount = getNewMessageCount(DMId(dm[DMsTable.id])),
        )
    }

    override suspend fun insertDM(dm: DM): ApiResult<Unit, DomainException> =
        runCatchWithContext(dispatcher) {
            suspendTransaction(database) {
                DMsTable
                    .insert {
                        it[id] = dm.id.value
                        it[name] = dm.name.value
                        it[userEmail] = dm.userEmail.value
                    }
                AvatarsTable
                    .insertIgnore { it[email] = dm.to.email.value }
                DMsAvatarsTable
                    .insert {
                        it[id] = UUID.randomUUID().toString()
                        it[dmId] = dm.id.value
                        it[avatarEmail] = dm.to.email.value
                    }
            }
        }

    private fun getNewMessageCount(dmId: DMId) = NewMessageCount(
        transaction(database) {
            DMMessagesTable
                .select { DMMessagesTable.id eq dmId.value }
                .count { it[DMMessagesTable.isRecent] }
        },
    )
}
