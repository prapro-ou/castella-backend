package repository.table

import org.jetbrains.exposed.sql.Table

object GroupsAvatarsTable : Table("groups_avatars") {
    val id = varchar("id", 256)
    val groupId = varchar("group_id", 256)
    val avatarId = varchar("avatar_id", 256)

    override val primaryKey: PrimaryKey = PrimaryKey(id)
}