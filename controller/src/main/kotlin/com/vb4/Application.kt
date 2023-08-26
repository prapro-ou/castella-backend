package com.vb4

import com.vb4.dm.DMId
import com.vb4.mail.imap.Imap
import com.vb4.plugins.installPlugins
import com.vb4.result.ApiResult
import com.vb4.routing.mainRoute
import db.DevDB
import db.table.AvatarsTable
import db.table.DMMessagesTable
import db.table.DMsAvatarsTable
import db.table.DMsTable
import db.table.UsersTable
import db.table.toAuthUser
import db.table.toDM
import io.ktor.server.application.Application
import io.ktor.server.netty.EngineMain
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toJavaLocalDateTime
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.exposed.sql.batchInsert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

fun main(args: Array<String>) {
    GlobalScope.launch {
        while (true) {
            updateDMMessages()
            delay(10000L)
        }
    }

    EngineMain.main(args)
}

fun Application.module() {
    installPlugins()
    mainRoute()
}

suspend fun updateDMMessages(): ApiResult<Unit, DomainException> =
    runCatchWithContext(Dispatchers.IO) {
        val database = DevDB
        val users = transaction(database) {
            UsersTable.selectAll().map { it.toAuthUser() }
        }
        for (user in users) {
            val imap = Imap.Gmail(user.email.value, user.password.value)
            val dms = transaction(database) {
                DMsTable
                    .innerJoin(DMsAvatarsTable)
                    .innerJoin(AvatarsTable)
                    .select { DMsTable.userEmail eq user.email.value }
                    .map { dm ->
                        dm.toDM(
                            newMessageCount = getNewMessageCount(DMId(dm[DMsTable.id])),
                        )
                    }
            }

            for (dm in dms) {
                val dmMessageIds = transaction(database) {
                    DMMessagesTable
                        .select { DMMessagesTable.dmId eq dm.id.value }
                        .map { it[DMMessagesTable.id] }
                }
                println("dmMessageIds: ${dmMessageIds.joinToString()}")
                val fetchedMessages = imap.search {
                    dm(dm.userEmail.value, dm.to.email.value)
                    dmMessageIds.forEach { not { messageId(it) } }
                }
                println("fetch: ${fetchedMessages.count()}")
                transaction(database) {
                    DMMessagesTable.batchInsert(fetchedMessages) {
                        this[DMMessagesTable.id] = it.id
                        this[DMMessagesTable.dmId] = dm.id.value
                        this[DMMessagesTable.dmMessageId] = it.inReplyTo
                        this[DMMessagesTable.from] = it.from
                        this[DMMessagesTable.isRecent] = it.isRecent
                        this[DMMessagesTable.subject] = it.subject
                        this[DMMessagesTable.body] = it.body
                        this[DMMessagesTable.createdAt] = it.createdAt
                            .toLocalDateTime(TimeZone.UTC)
                            .toJavaLocalDateTime()
                    }
                }
            }
        }
    }

private fun getNewMessageCount(dmId: DMId) = NewMessageCount(
    transaction(DevDB) {
        DMMessagesTable
            .select { DMMessagesTable.id eq dmId.value }
            .count { it[DMMessagesTable.isRecent] }
    }
)
