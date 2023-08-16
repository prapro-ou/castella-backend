package com.vb4.mail.smtp

import javax.mail.internet.InternetAddress

data class SmtpMail(
    val id: String,
    val from: InternetAddress,
    val to: List<InternetAddress>,
    val cc: List<InternetAddress>? = null,
    val bcc: List<InternetAddress>? = null,
    val inReplyTo: String? = null,
    val subject: String,
    val body: String,
) {
    constructor(
        id: String,
        from: InternetAddress,
        to: InternetAddress,
        cc: List<InternetAddress>? = null,
        bcc: List<InternetAddress>? = null,
        inReplyTo: String? = null,
        subject: String,
        body: String,
    ) : this(
        id = id,
        from = from,
        to = listOf(to),
        cc = cc,
        bcc = bcc,
        inReplyTo = inReplyTo,
        subject = subject,
        body = body,
    )

    companion object
}
