package com.vb4.mail.imap.query.term

import javax.mail.Message
import javax.mail.search.SearchTerm

data object NothingTerm : SearchTerm() {
    override fun match(msg: Message?): Boolean = true
}
