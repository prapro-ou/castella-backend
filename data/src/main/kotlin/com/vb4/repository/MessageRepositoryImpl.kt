package com.vb4.repository

import com.vb4.DomainException
import com.vb4.Email
import com.vb4.avatar.Avatar
import com.vb4.dm.DM
import com.vb4.group.Group
import com.vb4.message.Body
import com.vb4.message.CreatedAt
import com.vb4.message.Message
import com.vb4.message.MessageId
import com.vb4.message.MessageRepository
import com.vb4.message.Reply
import com.vb4.message.Subject
import com.vb4.result.ApiResult
import javax.mail.Folder
import javax.mail.Session
import javax.mail.search.FromStringTerm
import javax.mail.search.OrTerm
import javax.mail.search.RecipientStringTerm
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.datetime.toKotlinInstant
import repository.com.vb4.runCatchDomainException

class MessageRepositoryImpl(
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
) : MessageRepository {

    private val folder by lazy {
        Session
            .getInstance(System.getProperties(), null)
            .getStore("imaps")
            .apply {
                connect("imap.gmail.com", 993, "inputUserEmail", "inputUserPassword")
            }
            .getFolder("INBOX")
            .apply { open(Folder.READ_ONLY) }
    }
    override suspend fun getMessagesByDM(dm: DM): ApiResult<List<Message>, DomainException> =
        withContext(dispatcher) {
            runCatchDomainException {
                val messages = folder
                    .search(
                        OrTerm(
                            FromStringTerm(dm.to.email.value),
                            RecipientStringTerm(javax.mail.Message.RecipientType.TO, dm.to.email.value)
                        )
                    )
                    .toList()
                messages.map { it.toDomainMessage(listOf()) }
            }
    }

    override suspend fun getMessagesByGroup(group: Group): ApiResult<List<Message>, DomainException> {
        TODO("Not yet implemented")
    }

    override suspend fun getMessageById(messageId: MessageId): ApiResult<Message, DomainException> {
        TODO("Not yet implemented")
    }

    private fun javax.mail.Message.toDomainMessage(replies: List<Reply>) = Message(
        id = MessageId(getHeader("Message-ID").getOrNull(0).orEmpty()),
        subject = Subject(subject.orEmpty()),
        body = Body(content.toString()),
        createdAt = CreatedAt(sentDate.toInstant().toKotlinInstant()),
        sender = Avatar(Email(getRecipients(javax.mail.Message.RecipientType.TO)?.getOrNull(0)?.toString() ?: "")),
        replies = replies,
    )
}
