package db.table

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.datetime
import org.jetbrains.exposed.sql.javatime.time

object DMMessagesTable : Table("dm_messages") {
    val id = varchar("id", 256)
    val dmId = reference("dm_id", DMsTable.id)
    val dmMessageId = reference("dm_message_id", id).nullable()
    val from = varchar("from", 256)
    val subject = varchar("subject", 256)
    val body = text("body")
    val isRecent = bool("is_recent")
    val createdAt = datetime("created_at")

    override val primaryKey: PrimaryKey = PrimaryKey(id)
}
