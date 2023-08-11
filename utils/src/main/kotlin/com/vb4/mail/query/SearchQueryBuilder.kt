package com.vb4.mail.query

import com.vb4.mail.query.term.NothingTerm
import com.vb4.mail.query.term.ReplyToStringTerm
import java.text.SimpleDateFormat
import javax.mail.Message
import javax.mail.search.AndTerm
import javax.mail.search.FromStringTerm
import javax.mail.search.HeaderTerm
import javax.mail.search.MessageIDTerm
import javax.mail.search.OrTerm
import javax.mail.search.RecipientStringTerm
import javax.mail.search.SearchException
import javax.mail.search.SearchTerm
import javax.mail.search.SentDateTerm
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

class SearchQueryBuilder {
    private val query = mutableListOf<SearchTerm>()

    fun from(pattern: String) = query.add(FromStringTerm(pattern))

    fun to(pattern: String) = query.add(RecipientStringTerm(Message.RecipientType.TO, pattern))

    fun cc(pattern: String) = query.add(RecipientStringTerm(Message.RecipientType.CC, pattern))

    fun bcc(pattern: String) = query.add(RecipientStringTerm(Message.RecipientType.BCC, pattern))

    fun replyTo(pattern: String) = query.add(ReplyToStringTerm(pattern))

    fun sentDate(comparison: Int, date: Instant, timeZone: TimeZone = TimeZone.UTC) {
        sentDate(comparison, date.toLocalDateTime(timeZone))
    }

    fun sentDate(comparison: Int, dateTime: LocalDateTime) {
        val javaDate = SimpleDateFormat("yyyy-MM-dd")
            .parse(dateTime.date.toString())
        query.add(SentDateTerm(comparison, javaDate))
    }

    fun or(block: SearchQueryBuilder.() -> Unit) {
        val imap = SearchQueryBuilder().apply(block)
        when (imap.query.size) {
            0 -> return
            1 -> query.add(imap.query.first())
            else -> imap
                .query
                .reduce { left, right -> OrTerm(left, right) }
                .also { query.add(it) }
        }
    }

    fun and(block: SearchQueryBuilder.() -> Unit) {
        val imap = SearchQueryBuilder().apply(block)
        when (imap.query.size) {
            0 -> return
            1 -> query.add(imap.query.first())
            else -> imap
                .query
                .reduce { left, right -> AndTerm(left, right) }
                .also { query.add(it) }
        }
    }

    fun messageId(id: String) {
        query.add(MessageIDTerm(id))
    }

    fun inReplyTo(id: String) {
        header("In-Reply-To", id)
    }

    fun header(headerName: String, pattern: Regex) {
        header(headerName, pattern.pattern)
    }

    fun header(headerName: String, pattern: String) {
        query.add(HeaderTerm(headerName, pattern))
    }

    internal fun build(): SearchTerm = when (query.size) {
        0 -> NothingTerm
        1 -> query.first()
        else -> query.reduce { left, right -> AndTerm(left, right) }
    }
}