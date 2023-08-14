package com.vb4.mail.smtp

import javax.mail.Message
import javax.mail.Session
import javax.mail.internet.MimeMessage

class MimeMessageWithMessageId(
    session: Session,
    private val messageId: String? = null,
) : MimeMessage(session) {
    override fun updateMessageID() {
        if (messageId == null) {
            super.updateMessageID()
        } else {
            setHeader("Message-ID", "<$messageId>")
        }
    }

    companion object {
        fun from(
            mail: SmtpMail,
            session: Session,
        ) = MimeMessageWithMessageId(session, mail.id).apply {
            setFrom(mail.from)
            setRecipient(Message.RecipientType.TO, mail.to)
            mail.cc?.let { cc -> setRecipients(Message.RecipientType.CC, cc.toTypedArray()) }
            mail.bcc?.let { bcc -> setRecipients(Message.RecipientType.BCC, bcc.toTypedArray()) }
            subject = mail.subject
            setText(mail.body)
            setHeader("In-Reply-To", mail.inReplyTo)
        }
    }
}
