package com.vb4.mail.smtp

import javax.mail.Authenticator
import javax.mail.Message
import javax.mail.PasswordAuthentication
import javax.mail.Session
import javax.mail.Transport
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeMessage

sealed interface Smtp {
    val session: Session

    class Gmail(user: String, password: String) : Smtp {
        override val session: Session by lazy {
            val props = System.getProperties().apply {
                setProperty("mail.smtp.host", "smtp.gmail.com")
                setProperty("mail.smtp.port", "587")
                setProperty("mail.smtp.auth", "true")
                setProperty("mail.smtp.starttls.enable", "true")
            }
            Session.getInstance(props, object : Authenticator() {
                override fun getPasswordAuthentication(): PasswordAuthentication {
                    return PasswordAuthentication(user, password)
                }
            })
        }
    }

    fun send(mail: SmtpMail) {
        MimeMessage(session).apply {
            setFrom(InternetAddress(mail.from, ""))
            setRecipient(Message.RecipientType.TO, InternetAddress(mail.to))
            mail.cc?.let { cc -> setRecipients(Message.RecipientType.CC, cc.toString()) }
            mail.bcc?.let { bcc -> setRecipients(Message.RecipientType.BCC, bcc.toString()) }
            subject = mail.subject
            setText(mail.body)
            setHeader("In-Reply-To", mail.inReplyTo)
        }
            .also { Transport.send(it) }
    }
}
