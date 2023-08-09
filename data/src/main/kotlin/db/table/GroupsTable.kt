package db.table

import com.vb4.Email
import com.vb4.group.Group
import com.vb4.group.GroupId
import com.vb4.group.GroupName
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.Table

object GroupsTable : Table("groups") {
    val id = varchar("id", 256)
    val name = varchar("name", 256)
    val userEmail = reference("user_email", UsersTable.email)

    override val primaryKey: PrimaryKey = PrimaryKey(id)
}

internal fun List<ResultRow>.toGroup() = Group(
    id = GroupId(this.first()[GroupsTable.id]),
    name = GroupName(this.first()[GroupsTable.name]),
    userEmail = Email(this.first()[GroupsTable.userEmail]),
    to = this.map { it.toAvatar() },
)
