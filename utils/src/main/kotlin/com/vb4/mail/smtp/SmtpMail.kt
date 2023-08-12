package com.vb4.mail.smtp

import javax.mail.internet.InternetAddress

data class SmtpMail(
    val from: InternetAddress,
    val to: InternetAddress,
    val cc: List<InternetAddress>? = null,
    val bcc: List<InternetAddress>? = null,
    val inReplyTo: String? = null,
    val subject: String,
    val body: String,
)
