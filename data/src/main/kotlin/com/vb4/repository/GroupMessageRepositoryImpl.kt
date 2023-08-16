package com.vb4.repository

import com.vb4.DomainException
import com.vb4.Email
import com.vb4.NewMessageCount
import com.vb4.avatar.Avatar
import com.vb4.group.Group
import com.vb4.group.GroupBody
import com.vb4.group.GroupCreatedAt
import com.vb4.group.GroupMessage
import com.vb4.group.GroupMessageId
import com.vb4.group.GroupMessageRepository
import com.vb4.group.GroupReply
import com.vb4.group.GroupSubject
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

class GroupMessageRepositoryImpl(
    private val imap: Imap,
    private val smtp: Smtp,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
) : GroupMessageRepository {
    override suspend fun getGroupMessages(
        group: Group,
    ): ApiResult<List<GroupMessage>, DomainException> = runCatchWithContext(dispatcher) {
        imap
            .search { group(group.userEmail.value, group.to.map { it.email.value }) }
            .groupingOriginalToReply()
            .map { (original, reply) ->
                original.toGroupMessage(reply.map { it.toGroupReply() })
            }
    }

    override suspend fun getGroupMessage(
        group: Group,
        messageId: GroupMessageId,
    ): ApiResult<GroupMessage, DomainException> = runCatchWithContext(dispatcher) {
        imap
            .search { group(group.userEmail.value, group.to.map { it.email.value }) }
            .groupingOriginalToReply()
            .first { (original, _) -> original.id == messageId.value }
            .let { (original, reply) ->
                original.toGroupMessage(reply.map { it.toGroupReply() })
            }
    }

    override suspend fun insertGroupMessage(
        group: Group,
        message: GroupMessage,
    ): ApiResult<Unit, DomainException> = runCatchWithContext(dispatcher) {
        smtp.send(SmtpMail.from(group, message))
    }

    override suspend fun insertGroupReply(
        group: Group,
        reply: GroupReply,
    ): ApiResult<Unit, DomainException> = runCatchWithContext(dispatcher) {
        smtp.send(SmtpMail.from(group, reply))
    }
}

private fun ImapMail.toGroupMessage(replies: List<GroupReply>) = GroupMessage.reconstruct(
    id = GroupMessageId(id),
    subject = GroupSubject(subject),
    body = GroupBody(body),
    createdAt = GroupCreatedAt(createdAt),
    from = Avatar.reconstruct(Email(from)),
    isRecent = isRecent,
    newMessageCount = NewMessageCount(replies.count { isRecent } + if (isRecent) 1 else 0),
    replies = replies,
)

private fun ImapMail.toGroupReply() = GroupReply.reconstruct(
    id = GroupMessageId(id),
    subject = GroupSubject(subject),
    from = Avatar.reconstruct(Email(from)),
    body = GroupBody(body),
    createdAt = GroupCreatedAt(createdAt),
    isRecent = isRecent,
)

private fun SmtpMail.Companion.from(group: Group, message: GroupMessage) = SmtpMail(
    id = message.id.value,
    from = InternetAddress(group.userEmail.value),
    to = group.to.map { InternetAddress(it.email.value) },
    subject = message.subject.value,
    body = message.body.value,
)

private fun SmtpMail.Companion.from(group: Group, reply: GroupReply) = SmtpMail(
    id = reply.id.value,
    from = InternetAddress(group.userEmail.value),
    to = group.to.map { InternetAddress(it.email.value) },
    subject = reply.subject.value,
    body = reply.body.value,
)
