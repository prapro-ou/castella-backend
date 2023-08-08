package table

import org.jetbrains.exposed.sql.Table

object DMsAvatarsTable : Table("dms_avatars") {
    val id = varchar("id", 256)
    val dmId = reference("dm_id", DMsTable.id).uniqueIndex()
    val avatarEmail = reference("avatar_email", AvatarsTable.email)

    override val primaryKey: PrimaryKey = PrimaryKey(id)
}