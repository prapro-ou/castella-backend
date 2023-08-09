package db.table

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.time

object GroupMessagesTable : Table("group_messages") {
    val id = varchar("id", 256)
    val groupId = reference("group_id", GroupsTable.id)
    val groupMessageID = reference("group_message_id", id).nullable()
    val from = varchar("from", 256)
    val to = varchar("to", 256)
    val subject = varchar("subject", 256)
    val body = text("body")
    val createdAt = time("created_at")

    override val primaryKey: PrimaryKey = PrimaryKey(id)
}
