package com.vb4.mail.smtp

import javax.mail.Authenticator
import javax.mail.PasswordAuthentication
import javax.mail.Session
import javax.mail.Transport

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
        MimeMessageWithMessageId.from(mail, session).also { Transport.send(it) }
    }
}
