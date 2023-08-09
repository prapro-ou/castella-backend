package com.vb4.repository

import com.vb4.DomainException
import com.vb4.Email
import com.vb4.avatar.Avatar
import com.vb4.dm.DM
import com.vb4.group.Group
import com.vb4.mail.Imap
import com.vb4.mail.Mail
import com.vb4.message.Body
import com.vb4.message.CreatedAt
import com.vb4.message.Message
import com.vb4.message.MessageId
import com.vb4.message.MessageRepository
import com.vb4.message.Reply
import com.vb4.message.Subject
import com.vb4.result.ApiResult
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import repository.com.vb4.runCatchDomainException

class MessageRepositoryImpl(
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
) : MessageRepository {

    private val imap = Imap.Gmail("user", "pass")

    override suspend fun getMessagesByDM(dm: DM): ApiResult<List<Message>, DomainException> =
        withContext(dispatcher) {
            runCatchDomainException {
                val messages = imap.search {
                    or {
                        from(email = dm.to.email.value)
                        to(email = dm.to.email.value)
                    }
                }
                messages.map { it.toMessage(listOf()) }
            }
        }

    override suspend fun getMessagesByGroup(group: Group): ApiResult<List<Message>, DomainException> {
        TODO("Not yet implemented")
    }

    override suspend fun getMessageById(
        messageId: MessageId,
    ): ApiResult<Message, DomainException> = withContext(dispatcher) {
        runCatchDomainException {
            val message = imap.getMessageById(messageId.value)
                ?: throw DomainException.NoSuchElementException("")
            val replies = imap.search {inReplyTo(messageId.value) }.map { it.toReply() }
            message.toMessage(replies)
        }
    }

    private fun Mail.toMessage(replies: List<Reply>) = Message(
        id = MessageId(id),
        subject = Subject(subject),
        body = Body(body),
        createdAt = CreatedAt(createdAt),
        from = Avatar(Email(from)),
        replies = replies,
    )

    private fun Mail.toReply() = Reply(
        id = MessageId(id),
        subject = Subject(subject),
        body = Body(body),
        createdAt = CreatedAt(createdAt),
        from = Avatar(Email(from)),
    )
}
