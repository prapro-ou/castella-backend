package com.vb4.repository

import com.vb4.DomainException
import com.vb4.Email
import com.vb4.NewMessageCount
import com.vb4.avatar.Avatar
import com.vb4.dm.DM
import com.vb4.dm.DMBody
import com.vb4.dm.DMCreatedAt
import com.vb4.dm.DMMessage
import com.vb4.dm.DMMessageId
import com.vb4.dm.DMMessageRepository
import com.vb4.dm.DMReply
import com.vb4.dm.DMSubject
import com.vb4.mail.imap.Imap
import com.vb4.mail.imap.ImapMail
import com.vb4.mail.imap.ImapMail.Companion.groupingOriginalToReply
import com.vb4.mail.smtp.Smtp
import com.vb4.mail.smtp.SmtpMail
import com.vb4.repository.DMMessageDto.Companion.toDMMessageDto
import com.vb4.repository.DMMessageDto.Companion.toDomain
import com.vb4.result.ApiResult
import com.vb4.runCatchWithContext
import db.table.DMMessagesTable
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.mail.internet.InternetAddress
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toJavaLocalDateTime
import kotlinx.datetime.toKotlinLocalDateTime
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.batchInsert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction

class DMMessageRepositoryImpl(
    private val database: Database,
    private val imap: Imap,
    private val smtp: Smtp,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
) : DMMessageRepository {
    override suspend fun getDMMessages(dm: DM): ApiResult<List<DMMessage>, DomainException> =
        runCatchWithContext(dispatcher) {

            val dbDMMessages = transaction(database) {
                DMMessagesTable
                    .select { DMMessagesTable.dmId eq dm.id.value }
                    .toList()
                    .map { it.toDMMessageDto() }
            }

            if(dbDMMessages.isEmpty()) {
                val imapDMMessage = imap.search {
                    dm(dm.userEmail.value, dm.to.email.value)
                    dbDMMessages.forEach {
                        not { messageId(it.id) }
                    }
                }
                    .map { it.toDMMessageDto() }

                transaction(database) {
                    DMMessagesTable.batchInsert(imapDMMessage) {
                        this[DMMessagesTable.id] = it.id
                        this[DMMessagesTable.dmId] = dm.id.value
                        this[DMMessagesTable.dmMessageId] = it.inReplyTo
                        this[DMMessagesTable.from] = it.from
                        this[DMMessagesTable.subject] = it.subject
                        this[DMMessagesTable.body] = it.body
                        this[DMMessagesTable.isRecent] = true
                        this[DMMessagesTable.createdAt] = it.createdAt
                            .toLocalDateTime(TimeZone.UTC)
                            .toJavaLocalDateTime()
                    }
                }
            }

            return@runCatchWithContext transaction(database) {
                DMMessagesTable
                    .select { DMMessagesTable.dmId eq dm.id.value }
                    .toList()
            }
                .map { it.toDMMessageDto() }
                .toDomain()
        }

    override suspend fun getDMMessage(
        dm: DM,
        messageId: DMMessageId,
    ): ApiResult<DMMessage, DomainException> = runCatchWithContext(dispatcher) {
        imap.search { dm(dm.userEmail.value, dm.to.email.value) }
            .groupingOriginalToReply()
            .first { (original, _) -> original.id == messageId.value }
            .let { (original, replies) ->
                original.toDMMessage(replies = replies.map { it.toDMReply() })
            }
    }

    override suspend fun insertDMMessage(
        dm: DM,
        message: DMMessage,
    ): ApiResult<Unit, DomainException> = runCatchWithContext(dispatcher) {
        smtp.send(SmtpMail.from(dm, message))
    }

    override suspend fun insertDMReply(
        dm: DM,
        reply: DMReply,
    ): ApiResult<Unit, DomainException> = runCatchWithContext(dispatcher) {
        smtp.send(SmtpMail.from(dm, reply))
    }
}

private fun ImapMail.toDMMessage(replies: List<DMReply>) = DMMessage.reconstruct(
    id = DMMessageId(id),
    subject = DMSubject(subject),
    body = DMBody(body),
    createdAt = DMCreatedAt(createdAt),
    from = Avatar.reconstruct(Email(from)),
    isRecent = isRecent,
    newMessageCount = NewMessageCount(replies.count { isRecent } + if (isRecent) 1 else 0),
    replies = replies,
)

private fun ImapMail.toDMReply() = DMReply.reconstruct(
    id = DMMessageId(id),
    subject = DMSubject(subject),
    body = DMBody(body),
    createdAt = DMCreatedAt(createdAt),
    isRecent = isRecent,
    from = Avatar.reconstruct(Email(from)),
)

private fun SmtpMail.Companion.from(dm: DM, message: DMMessage) = SmtpMail(
    id = message.id.value,
    from = InternetAddress(dm.userEmail.value),
    to = InternetAddress(dm.to.email.value),
    subject = message.subject.value,
    body = message.body.value,
)

private fun SmtpMail.Companion.from(dm: DM, reply: DMReply) = SmtpMail(
    id = reply.id.value,
    from = InternetAddress(dm.userEmail.value),
    to = InternetAddress(dm.to.email.value),
    inReplyTo = dm.to.email.value,
    subject = reply.subject.value,
    body = reply.body.value,
)

data class DMMessageDto(
    val id: String,
    val from: String,
    val subject: String,
    val body: String,
    val inReplyTo: String?,
    val isRecent: Boolean,
    val createdAt: Instant,
) {

    fun toDMMessage(replies: List<DMReply>): DMMessage = DMMessage.reconstruct(
        id = DMMessageId(id),
        subject = DMSubject(subject),
        body = DMBody(body),
        createdAt = DMCreatedAt(createdAt),
        isRecent = isRecent,
        from = Avatar.reconstruct(Email(from)),
        newMessageCount = NewMessageCount(replies.count { isRecent } + if (isRecent) 1 else 0),
        replies = replies,
    )

    fun toDMReply() = DMReply.reconstruct(
        id = DMMessageId(id),
        subject = DMSubject(subject),
        body = DMBody(body),
        createdAt = DMCreatedAt(createdAt),
        isRecent = isRecent,
        from = Avatar.reconstruct(Email(from)),
    )

    companion object {
        fun ImapMail.toDMMessageDto() = DMMessageDto(
            id = id,
            from = from,
            subject = subject,
            body = body,
            inReplyTo = inReplyTo,
            isRecent = isRecent,
            createdAt = createdAt,
        )

        fun ResultRow.toDMMessageDto() = DMMessageDto(
            id = this[DMMessagesTable.id],
            from = this[DMMessagesTable.from],
            subject = this[DMMessagesTable.subject],
            body = this[DMMessagesTable.body],
            inReplyTo = this[DMMessagesTable.dmMessageId],
            isRecent = this[DMMessagesTable.isRecent],
            createdAt = this[DMMessagesTable.createdAt].toKotlinLocalDateTime().toInstant(TimeZone.UTC),
        )

        fun List<DMMessageDto>.toDomain(): List<DMMessage> {
            val messages = this.groupBy { it.inReplyTo }
            return (messages[null] ?: emptyList())
                .fold(emptyList<Pair<DMMessageDto, List<DMMessageDto>>>()) { acc, original ->
                    acc + (original to (messages[original.id] ?: emptyList()))
                }
                .map { (original, replies) ->
                    original.toDMMessage(replies.map { it.toDMReply() })
                }
        }
    }
}
