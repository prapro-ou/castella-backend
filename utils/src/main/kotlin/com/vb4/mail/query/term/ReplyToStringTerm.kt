package com.vb4.mail.query.term

import javax.mail.Message
import javax.mail.search.AddressStringTerm
import javax.mail.search.StringTerm

data class ReplyToStringTerm(private val pattern: String) : AddressStringTerm(pattern) {
    override fun match(msg: Message): Boolean {
        msg.replyTo.forEach { if(super.match(it)) return true }
        return false
    }
}