package repository.table

import org.jetbrains.exposed.sql.Table

object DMsAvatarsTable : Table("dms_avatars") {
    val id = varchar("id", 256)
    val dmId = varchar("dm_id", 256)
    val avatarId = varchar("avatar_id", 256)

    override val primaryKey: PrimaryKey = PrimaryKey(id)
}