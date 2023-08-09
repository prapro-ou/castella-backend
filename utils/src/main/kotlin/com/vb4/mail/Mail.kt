package com.vb4.mail

import javax.mail.Message
import kotlinx.datetime.Instant
import kotlinx.datetime.toKotlinInstant

class Mail private constructor(private val message: Message) {

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
    val to: String by lazy {
        message
            .getRecipients(Message.RecipientType.TO)
            ?.getOrNull(0)
            ?.toString() ?: ""
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
    val body: String by lazy { message.content.toString() }
    val createdAt: Instant by lazy { message.sentDate.toInstant().toKotlinInstant() }

    companion object {
        fun from(message: Message) = Mail(message)

        fun List<Mail>.groupingOriginalToReply(): List<Pair<Mail, List<Mail>>> {
            val mails = this.groupBy { it.inReplyTo }
            return (mails[""] ?: emptyList())
                .fold(emptyList()) { acc, original ->
                    acc + (original to (mails[original.id] ?: emptyList()))
                }
        }
    }
}
