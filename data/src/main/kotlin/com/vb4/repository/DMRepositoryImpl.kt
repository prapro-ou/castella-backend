package com.vb4.repository

import com.vb4.DomainException
import com.vb4.Email
import com.vb4.avatar.Avatar
import com.vb4.dm.DM
import com.vb4.dm.DMBody
import com.vb4.dm.DMCreatedAt
import com.vb4.dm.DMId
import com.vb4.dm.DMMessage
import com.vb4.dm.DMMessageId
import com.vb4.dm.DMReply
import com.vb4.dm.DMRepository
import com.vb4.dm.DMSubject
import com.vb4.mail.imap.Imap
import com.vb4.mail.imap.ImapMail
import com.vb4.mail.imap.ImapMail.Companion.groupingOriginalToReply
import com.vb4.result.ApiResult
import com.vb4.result.flatMap
import com.vb4.runCatchWithContext
import com.vb4.suspendTransaction
import db.table.AvatarsTable
import db.table.DMsAvatarsTable
import db.table.DMsTable
import db.table.toDM
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction

class DMRepositoryImpl(
    private val database: Database,
    private val imap: Imap,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
) : DMRepository {
    override suspend fun getDM(
        dmId: DMId,
    ): ApiResult<DM, DomainException> = runCatchWithContext(dispatcher) {
        suspendTransaction(database) {
            DMsTable
                .innerJoin(DMsAvatarsTable)
                .innerJoin(AvatarsTable)
                .select { DMsTable.id eq dmId.value }
                .first()
                .toDM()
        }
    }

    override suspend fun getDMMessages(dm: DM): ApiResult<List<DMMessage>, DomainException> =
        runCatchWithContext(dispatcher) {
            imap.search { dm(dm.userEmail.value, dm.to.email.value) }
                .groupingOriginalToReply()
                .map { (original, replies) ->
                    original.toDMMessage(replies = replies.map { it.toDMReply() })
                }
        }

    override suspend fun getDMMessage(
        dmId: DMId,
        messageId: DMMessageId,
    ): ApiResult<DMMessage, DomainException> = runCatchWithContext(dispatcher) {
        val message = imap.getMessageById(messageId.value)
            ?: throw DomainException.NoSuchElementException("")
        val replies = imap.search { inReplyTo(messageId.value) }.map { it.toDMReply() }
        message.toDMMessage(replies)
    }

    private fun ImapMail.toDMMessage(replies: List<DMReply>) = DMMessage(
        id = DMMessageId(id),
        subject = DMSubject(subject),
        body = DMBody(body),
        createdAt = DMCreatedAt(createdAt),
        from = Avatar(Email(from)),
        replies = replies,
    )

    private fun ImapMail.toDMReply() = DMReply(
        id = DMMessageId(id),
        subject = DMSubject(subject),
        body = DMBody(body),
        createdAt = DMCreatedAt(createdAt),
        from = Avatar(Email(from)),
    )
}
