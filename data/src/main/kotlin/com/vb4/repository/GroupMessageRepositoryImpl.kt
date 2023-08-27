package com.vb4.repository

import com.vb4.DomainException
import com.vb4.datetime.toJavaLocalDateTime
import com.vb4.group.Group
import com.vb4.group.GroupMessage
import com.vb4.group.GroupMessageId
import com.vb4.group.GroupMessageRepository
import com.vb4.group.GroupReply
import com.vb4.mail.smtp.Smtp
import com.vb4.mail.smtp.SmtpMail
import com.vb4.result.ApiResult
import com.vb4.runCatchWithContext
import com.vb4.suspendTransactionAsync
import db.table.GroupMessagesTable
import db.table.toGroupMessage
import db.table.toGroupMessages
import db.table.toGroupReply
import javax.mail.internet.InternetAddress
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.andWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction

class GroupMessageRepositoryImpl(
    private val database: Database,
    private val smtp: Smtp,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
) : GroupMessageRepository {
    override suspend fun getGroupMessages(
        group: Group,
    ): ApiResult<List<GroupMessage>, DomainException> = runCatchWithContext(dispatcher) {
        transaction(database) {
            GroupMessagesTable
                .select { GroupMessagesTable.groupId eq group.id.value }
                .toList()
        }.toGroupMessages()
    }

    override suspend fun getGroupMessage(
        group: Group,
        messageId: GroupMessageId,
    ): ApiResult<GroupMessage, DomainException> = runCatchWithContext(dispatcher) {
        val original = suspendTransactionAsync(database) {
            GroupMessagesTable
                .select { GroupMessagesTable.groupId eq group.id.value }
                .andWhere { GroupMessagesTable.id eq messageId.value }
                .first()
        }
        val replies = suspendTransactionAsync(database) {
            GroupMessagesTable
                .select { GroupMessagesTable.groupId eq group.id.value }
                .andWhere { GroupMessagesTable.groupMessageId eq messageId.value }
                .toList()
        }
        original.await().toGroupMessage(replies = replies.await().map { it.toGroupReply() })
    }

    override suspend fun insertGroupMessage(
        group: Group,
        message: GroupMessage,
    ): ApiResult<Unit, DomainException> = runCatchWithContext(dispatcher) {
        smtp.send(SmtpMail.from(group, message))
        transaction(database) {
            GroupMessagesTable.insert {
                it[id] = message.id.value
                it[groupId] = group.id.value
                it[groupMessageId] = null
                it[from] = message.from.email.value
                it[subject] = message.subject.value
                it[body] = message.body.value
                it[isRecent] = message.isRecent
                it[createdAt] = message.createdAt.value.toJavaLocalDateTime()
            }
        }
    }

    override suspend fun insertGroupReply(
        group: Group,
        inReplyTo: GroupMessageId,
        reply: GroupReply,
    ): ApiResult<Unit, DomainException> = runCatchWithContext(dispatcher) {
        smtp.send(SmtpMail.from(group, inReplyTo, reply))
        GroupMessagesTable.insert {
            it[id] = reply.id.value
            it[groupId] = group.id.value
            it[groupMessageId] = inReplyTo.value
            it[from] = reply.from.email.value
            it[subject] = reply.subject.value
            it[body] = reply.body.value
            it[isRecent] = reply.isRecent
            it[createdAt] = reply.createdAt.value.toJavaLocalDateTime()
        }
    }
}

private fun SmtpMail.Companion.from(group: Group, message: GroupMessage) = SmtpMail(
    id = message.id.value,
    from = InternetAddress(group.userEmail.value),
    to = group.to.map { InternetAddress(it.email.value) },
    subject = message.subject.value,
    body = message.body.value,
)

private fun SmtpMail.Companion.from(group: Group, inReplyTo: GroupMessageId, reply: GroupReply) = SmtpMail(
    id = reply.id.value,
    from = InternetAddress(group.userEmail.value),
    to = group.to.map { InternetAddress(it.email.value) },
    inReplyTo = inReplyTo.value,
    subject = reply.subject.value,
    body = reply.body.value,
)
