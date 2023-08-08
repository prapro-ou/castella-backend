package table

import org.jetbrains.exposed.sql.Table

object GroupsAvatarsTable : Table("groups_avatars") {
    val id = varchar("id", 256)
    val groupId = reference("group_id", GroupsTable.id)
    val avatarEmail = reference("avatar_email", AvatarsTable.email)

    override val primaryKey: PrimaryKey = PrimaryKey(id)
}