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
        }
    }

    fun search(block: SearchQueryBuilder.() -> Unit):List<Message> = SearchQueryBuilder()
        .apply(block)
        .build()
        .let { folder.search(it) }
        .toList()

    fun hasNewMessage() = folder.hasNewMessages()

    fun getMessageById(messageId: String): Message? = search { messageId(messageId) }.firstOrNull()
}
