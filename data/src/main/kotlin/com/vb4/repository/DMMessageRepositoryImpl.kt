package com.vb4.repository

import com.vb4.DomainException
import com.vb4.Email
import com.vb4.avatar.Avatar
import com.vb4.dm.DM
import com.vb4.dm.DMBody
import com.vb4.dm.DMCreatedAt
import com.vb4.dm.DMId
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
import com.vb4.result.ApiResult
import com.vb4.runCatchWithContext
import javax.mail.internet.InternetAddress
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

class DMMessageRepositoryImpl(
    private val imap: Imap,
    private val smtp: Smtp,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
) : DMMessageRepository {
    override suspend fun getDMMessages(dm: DM): ApiResult<List<DMMessage>, DomainException> =
        runCatchWithContext(dispatcher) {
            imap.search { dm(dm.userEmail.value, dm.to.email.value) }
                .groupingOriginalToReply()
                .map { (original, replies) ->
                    original.toDMMessage(replies = replies.map { it.toDMReply() })
                }
        }

    override suspend fun getDMMessage(
        dm: DM,
        messageId: DMMessageId,
    ): ApiResult<DMMessage, DomainException> = runCatchWithContext(dispatcher) {
        val message = imap.getMessageById(messageId.value)
            ?: throw DomainException.NoSuchElementException("")
        val replies = imap.search { inReplyTo(messageId.value) }.map { it.toDMReply() }
        message.toDMMessage(replies)
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
    replies = replies,
)

private fun ImapMail.toDMReply() = DMReply.reconstruct(
    id = DMMessageId(id),
    subject = DMSubject(subject),
    body = DMBody(body),
    createdAt = DMCreatedAt(createdAt),
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