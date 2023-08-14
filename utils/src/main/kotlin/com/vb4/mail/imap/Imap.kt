package com.vb4.mail.imap

import com.vb4.mail.imap.query.SearchQueryBuilder
import javax.mail.FetchProfile
import javax.mail.Folder
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

    fun search(block: SearchQueryBuilder.() -> Unit): List<ImapMail> = SearchQueryBuilder()
        .apply(block)
        .build()
        .let { term -> folder.search(term) }
        .also { messages -> folder.fetch(messages, fetchProfile) }
        .map { ImapMail.from(it) }

    fun getMessageById(messageId: String): ImapMail? = search { messageId(messageId) }.firstOrNull()
}

private val fetchProfile = FetchProfile().apply {
    add(FetchProfile.Item.ENVELOPE)
    add(FetchProfile.Item.CONTENT_INFO)
    add(FetchProfile.Item.FLAGS)
    add(FetchProfile.Item.SIZE)
    add("Message-ID")
    add("In-Reply-To")
}
