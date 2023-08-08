package db.table

import com.vb4.avatar.Avatar
import com.vb4.destination.Destination
import com.vb4.destination.DestinationId
import com.vb4.destination.DestinationName
import com.vb4.user.Email
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.Table

object DMsTable : Table("dms") {
    val id = varchar("id", 256)
    val name = varchar("name", 256)
    val userEmail = reference("user_email", UsersTable.email)

    override val primaryKey: PrimaryKey = PrimaryKey(id)
}

internal fun ResultRow.toDM() = Destination.DM(
    id = DestinationId(this[DMsTable.id]),
    name = DestinationName(this[DMsTable.name]),
    to = Avatar(Email(this[AvatarsTable.email])),
)
