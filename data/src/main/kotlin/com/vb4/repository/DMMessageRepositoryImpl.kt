package com.vb4.repository

import com.vb4.DomainException
import com.vb4.dm.DM
import com.vb4.dm.DMMessage
import com.vb4.dm.DMMessageId
import com.vb4.dm.DMMessageRepository
import com.vb4.dm.DMReply
import com.vb4.mail.smtp.Smtp
import com.vb4.mail.smtp.SmtpMail
import com.vb4.result.ApiResult
import com.vb4.runCatchWithContext
import com.vb4.suspendTransactionAsync
import db.table.DMMessagesTable
import db.table.toDMMessage
import db.table.toDMMessages
import db.table.toDMReply
import javax.mail.internet.InternetAddress
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toJavaLocalDateTime
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.andWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction

class DMMessageRepositoryImpl(
    private val database: Database,
    private val smtp: Smtp,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
) : DMMessageRepository {
    override suspend fun getDMMessages(dm: DM): ApiResult<List<DMMessage>, DomainException> =
        runCatchWithContext(dispatcher) {
            transaction(database) {
                DMMessagesTable
                    .select { DMMessagesTable.dmId eq dm.id.value }
                    .toList()
            }
                .toDMMessages()
        }

    override suspend fun getDMMessage(
        dm: DM,
        messageId: DMMessageId,
    ): ApiResult<DMMessage, DomainException> = runCatchWithContext(dispatcher) {
        val original = suspendTransactionAsync(database) {
            DMMessagesTable
                .select { DMMessagesTable.dmId eq dm.id.value }
                .andWhere { DMMessagesTable.id eq messageId.value }
                .first()
        }
        val replies = suspendTransactionAsync(database) {
            DMMessagesTable
                .select { DMMessagesTable.dmId eq dm.id.value }
                .andWhere { DMMessagesTable.dmMessageId eq messageId.value }
                .toList()
        }
        original.await().toDMMessage(replies = replies.await().map { it.toDMReply() })
    }

    override suspend fun insertDMMessage(
        dm: DM,
        message: DMMessage,
    ): ApiResult<Unit, DomainException> = runCatchWithContext(dispatcher) {
        smtp.send(SmtpMail.from(dm, message))
        transaction(database) {
            DMMessagesTable.insert {
                it[id] = message.id.value
                it[dmId] = dm.id.value
                it[dmMessageId] = null
                it[from] = message.from.email.value
                it[subject] = message.subject.value
                it[body] = message.body.value
                it[isRecent] = message.isRecent
                it[createdAt] = message.createdAt.value.toLocalDateTime(TimeZone.UTC).toJavaLocalDateTime()
            }
        }
    }

    override suspend fun insertDMReply(
        dm: DM,
        inReplyTo: DMMessageId,
        reply: DMReply,
    ): ApiResult<Unit, DomainException> = runCatchWithContext(dispatcher) {
        smtp.send(SmtpMail.from(dm, inReplyTo, reply))
        transaction(database) {
            DMMessagesTable.insert {
                it[id] = reply.id.value
                it[dmId] = dm.id.value
                it[dmMessageId] = inReplyTo.value
                it[from] = reply.from.email.value
                it[subject] = reply.subject.value
                it[body] = reply.body.value
                it[isRecent] = reply.isRecent
                it[createdAt] = reply.createdAt.value.toLocalDateTime(TimeZone.UTC).toJavaLocalDateTime()
            }
        }
    }
}

private fun SmtpMail.Companion.from(dm: DM, message: DMMessage) = SmtpMail(
    id = message.id.value,
    from = InternetAddress(dm.userEmail.value),
    to = InternetAddress(dm.to.email.value),
    subject = message.subject.value,
    body = message.body.value,
)

private fun SmtpMail.Companion.from(dm: DM, inReplyTo: DMMessageId, reply: DMReply) = SmtpMail(
    id = reply.id.value,
    from = InternetAddress(dm.userEmail.value),
    to = InternetAddress(dm.to.email.value),
    inReplyTo = inReplyTo.value,
    subject = reply.subject.value,
    body = reply.body.value,
)
