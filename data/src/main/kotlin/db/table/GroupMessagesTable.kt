package db.table

import com.vb4.Email
import com.vb4.NewMessageCount
import com.vb4.avatar.Avatar
import com.vb4.datetime.toKotlinInstant
import com.vb4.group.GroupBody
import com.vb4.group.GroupCreatedAt
import com.vb4.group.GroupMessage
import com.vb4.group.GroupMessageId
import com.vb4.group.GroupReply
import com.vb4.group.GroupSubject
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.datetime

object GroupMessagesTable : Table("group_messages") {
    val id = varchar("id", 256)
    val groupId = reference("group_id", GroupsTable.id)
    val groupMessageId = reference("group_message_id", id).nullable()
    val from = varchar("from", 256)
    val to = varchar("to", 256)
    val subject = varchar("subject", 256)
    val body = text("body")
    val isRecent = bool("is_recent")
    val createdAt = datetime("created_at")

    override val primaryKey: PrimaryKey = PrimaryKey(id)
}

fun List<ResultRow>.toGroupMessages(): List<GroupMessage> {
    val messages = this.groupBy { it[GroupMessagesTable.groupMessageId] }
    return (messages[null] ?: emptyList())
        .fold(emptyList<Pair<ResultRow, List<ResultRow>>>()) { acc, original ->
            acc + (original to (messages[original[GroupMessagesTable.id]] ?: emptyList()))
        }
        .map { (original, replies) ->
            original.toGroupMessage(replies.map { it.toGroupReply() })
        }
}

fun ResultRow.toGroupMessage(replies: List<GroupReply>) = GroupMessage.reconstruct(
    id = GroupMessageId(this[GroupMessagesTable.id]),
    from = Avatar.reconstruct(email = Email(this[GroupMessagesTable.from])),
    subject = GroupSubject(this[GroupMessagesTable.subject]),
    body = GroupBody(this[GroupMessagesTable.body]),
    isRecent = this[GroupMessagesTable.isRecent],
    newMessageCount = NewMessageCount(replies.count { it.isRecent } + if (this[GroupMessagesTable.isRecent]) 1 else 0),
    replies = replies,
    createdAt = GroupCreatedAt(this[GroupMessagesTable.createdAt].toKotlinInstant()),
)

fun ResultRow.toGroupReply() = GroupReply.reconstruct(
    id = GroupMessageId(this[GroupMessagesTable.id]),
    from = Avatar.reconstruct(email = Email(this[GroupMessagesTable.from])),
    subject = GroupSubject(this[GroupMessagesTable.subject]),
    body = GroupBody(this[GroupMessagesTable.body]),
    isRecent = this[GroupMessagesTable.isRecent],
    createdAt = GroupCreatedAt(this[GroupMessagesTable.createdAt].toKotlinInstant()),
)
