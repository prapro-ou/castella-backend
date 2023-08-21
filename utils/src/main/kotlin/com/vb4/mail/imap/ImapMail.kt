package com.vb4.mail.imap

import kotlinx.datetime.Instant
import kotlinx.datetime.toKotlinInstant
import javax.mail.Flags
import javax.mail.Message
import javax.mail.internet.MimeMultipart

class ImapMail private constructor(private val message: Message) {

    val id: String by lazy {
        message
            .getHeader("Message-ID")
            ?.firstOrNull()
            .orEmpty()
    }
    val from: String by lazy {
        message
            .getRecipients(Message.RecipientType.TO)
            ?.getOrNull(0)
            ?.toString() ?: ""
    }
    val to: List<String> by lazy {
        message
            .getRecipients(Message.RecipientType.TO)
            ?.map { it.toString() }
            ?: emptyList()
    }
    val cc: List<String> by lazy {
        message
            .getRecipients(Message.RecipientType.CC)
            ?.map { it.toString() }
            ?: emptyList()
    }
    val bcc: List<String> by lazy {
        message
            .getRecipients(Message.RecipientType.BCC)
            ?.map { it.toString() } ?: emptyList()
    }
    val inReplyTo: String by lazy {
        message
            .getHeader("In-Reply-To")
            ?.firstOrNull()
            .orEmpty()
    }
    val subject: String by lazy { message.subject.orEmpty() }
    val body: String by lazy {
        (message.content as? MimeMultipart)
            ?.getBodyPart(0)
            ?.content
            ?.toString()
            ?: message.content.toString()
    }
    val isRecent: Boolean by lazy { message.flags.contains(Flags.Flag.RECENT) }
    val createdAt: Instant by lazy { message.sentDate.toInstant().toKotlinInstant() }

    companion object {
        fun from(message: Message) = ImapMail(message)

        fun List<ImapMail>.groupingOriginalToReply(): List<Pair<ImapMail, List<ImapMail>>> {
            val mails = this.groupBy { it.inReplyTo }
            return (mails[""] ?: emptyList())
                .fold(emptyList()) { acc, original ->
                    acc + (original to (mails[original.id] ?: emptyList()))
                }
        }
    }
}
