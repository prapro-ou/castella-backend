package db.table

import com.vb4.destination.Destination
import com.vb4.destination.DestinationId
import com.vb4.destination.DestinationName
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.Table

object GroupsTable : Table("groups") {
    val id = varchar("id", 256)
    val name = varchar("name", 256)
    val userEmail = reference("user_email", UsersTable.email)

    override val primaryKey: PrimaryKey = PrimaryKey(id)
}

internal fun List<ResultRow>.toGroup() = Destination.Group(
    id = DestinationId(this.first()[GroupsTable.id]),
    name = DestinationName(this.first()[GroupsTable.name]),
    to = this.map { it.toAvatar() },
)
