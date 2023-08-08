package db.table

import com.vb4.destination.Destination
import com.vb4.user.Email
import com.vb4.user.User
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.Table

object UsersTable : Table("users") {
    val email = varchar("email", 256)

    override val primaryKey: PrimaryKey = PrimaryKey(email)
}

internal fun ResultRow.toUser(dms: List<Destination.DM>, groups: List<Destination.Group>) = User(
    email = Email(this[UsersTable.email]),
    dms = dms,
    groups = groups
)
