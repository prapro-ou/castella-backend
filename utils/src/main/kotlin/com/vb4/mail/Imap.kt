package com.vb4.mail

import com.vb4.mail.query.SearchQueryBuilder
import javax.mail.Folder
import javax.mail.Message
import javax.mail.Session

sealed interface Imap {
    val folder: Folder

    class Gmail(user: String, password: String) : Imap {
        override val folder: Folder by lazy {
            Session
                .getInstance(System.getProperties(), null)
                .getStore("imaps")
                .apply { connect("imap.gmail.com", 993, user, password) }
                .getFolder("INBOX")
                .apply { open(Folder.READ_ONLY) }
        }
    }

    fun search(block: SearchQueryBuilder.() -> Unit) : List<Mail> = SearchQueryBuilder()
        .apply(block)
        .build()
        .let { term -> folder.search(term) }
        .map { Mail.from(it) }

    fun hasNewMessage() = folder.hasNewMessages()

    fun getMessageById(messageId: String): Mail? = search { messageId(messageId) }.firstOrNull()
}
