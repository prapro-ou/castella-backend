package db.table

import com.vb4.Email
import com.vb4.NewMessageCount
import com.vb4.avatar.Avatar
import com.vb4.datetime.toKotlinInstant
import com.vb4.dm.DMBody
import com.vb4.dm.DMCreatedAt
import com.vb4.dm.DMMessage
import com.vb4.dm.DMMessageId
import com.vb4.dm.DMReply
import com.vb4.dm.DMSubject
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.datetime

object DMMessagesTable : Table("dm_messages") {
    val id = varchar("id", 256)
    val dmId = reference("dm_id", DMsTable.id)
    val dmMessageId = varchar("dm_message_id", 256).nullable()
    val from = varchar("from", 256)
    val subject = varchar("subject", 256)
    val body = text("body")
    val isRecent = bool("is_recent")
    val createdAt = datetime("created_at")

    override val primaryKey: PrimaryKey = PrimaryKey(id)
}

fun List<ResultRow>.toDMMessages(): List<DMMessage> {
    val messages = this.groupBy { it[DMMessagesTable.dmMessageId] }
    return (messages[null] ?: emptyList())
        .fold(emptyList<Pair<ResultRow, List<ResultRow>>>()) { acc, original ->
            acc + (original to (messages[original[DMMessagesTable.id]] ?: emptyList()))
        }
        .map { (original, replies) ->
            original.toDMMessage(replies.map { it.toDMReply() })
        }
}

fun ResultRow.toDMMessage(replies: List<DMReply>) = DMMessage.reconstruct(
    id = DMMessageId(this[DMMessagesTable.id]),
    from = Avatar.reconstruct(email = Email(this[DMMessagesTable.from])),
    subject = DMSubject(this[DMMessagesTable.subject]),
    body = DMBody(this[DMMessagesTable.body]),
    isRecent = this[DMMessagesTable.isRecent],
    newMessageCount = NewMessageCount(replies.count { it.isRecent } + if (this[DMMessagesTable.isRecent]) 1 else 0),
    replies = replies,
    createdAt = DMCreatedAt(this[DMMessagesTable.createdAt].toKotlinInstant()),
)

fun ResultRow.toDMReply() = DMReply.reconstruct(
    id = DMMessageId(this[DMMessagesTable.id]),
    from = Avatar.reconstruct(email = Email(this[DMMessagesTable.from])),
    subject = DMSubject(this[DMMessagesTable.subject]),
    body = DMBody(this[DMMessagesTable.body]),
    isRecent = this[DMMessagesTable.isRecent],
    createdAt = DMCreatedAt(this[DMMessagesTable.createdAt].toKotlinInstant()),
)
