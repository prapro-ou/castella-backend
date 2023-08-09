package db.table

import com.vb4.avatar.Avatar
import com.vb4.Email
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.Table

object AvatarsTable : Table("avatars") {
    val email = varchar("email", 256)

    override val primaryKey: PrimaryKey = PrimaryKey(email)
}

internal fun ResultRow.toAvatar() = Avatar(Email(this[AvatarsTable.email]))
