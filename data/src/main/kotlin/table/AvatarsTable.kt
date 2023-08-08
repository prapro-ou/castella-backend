package repository.table

import org.jetbrains.exposed.sql.Table

object AvatarsTable : Table("avatars") {
    val email = varchar("email", 256)

    override val primaryKey: PrimaryKey = PrimaryKey(email)
}
