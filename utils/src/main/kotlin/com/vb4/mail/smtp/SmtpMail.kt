package com.vb4.mail.smtp

data class SmtpMail(
    val from: String,
    val to: String,
    val cc: List<String>? = null,
    val bcc: List<String>? = null,
    val inReplyTo: String? = null,
    val subject: String,
    val body: String,
)
