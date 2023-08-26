package com.vb4.mail.imap

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import javax.mail.Message
import javax.mail.Multipart

class ImapMail private constructor(message: Message) {

    val id: String =
        message
            .getHeader("Message-ID")
            ?.firstOrNull()
            .orEmpty()

    val from: String =
        message
            .from
            ?.getOrNull(0)
            ?.toString() ?: ""

    val to: List<String> =
        message
            .getRecipients(Message.RecipientType.TO)
            ?.map { it.toString() }
            ?: emptyList()

    val cc: List<String> =
        message
            .getRecipients(Message.RecipientType.CC)
            ?.map { it.toString() }
            ?: emptyList()

    val bcc: List<String> =
        message
            .getRecipients(Message.RecipientType.BCC)
            ?.map { it.toString() } ?: emptyList()

    val inReplyTo: String? =
        message
            .getHeader("In-Reply-To")
            ?.firstOrNull()

    val subject: String = message.subject.orEmpty()
    val body: String = when {
        message.contentType == "text/plain" -> message.content.toString()
        message.contentType.contains("multipart") ->
            (message.content as Multipart)
                .getBodyPart(0)
                .content
                .toString()
        else -> message.content.toString()
    }

    val isRecent: Boolean = false
    val createdAt: Instant = Clock.System.now()

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
