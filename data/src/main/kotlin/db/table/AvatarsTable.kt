package db.table

import com.vb4.Email
import com.vb4.avatar.Avatar
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.Table

object AvatarsTable : Table("avatars") {
    val email = varchar("email", 256)

    override val primaryKey: PrimaryKey = PrimaryKey(email)
}

internal fun ResultRow.toAvatar() = Avatar.reconstruct(Email(this[AvatarsTable.email]))
