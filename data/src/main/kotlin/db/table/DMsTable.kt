package db.table

import com.vb4.Email
import com.vb4.avatar.Avatar
import com.vb4.dm.DM
import com.vb4.dm.DMId
import com.vb4.dm.DMName
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.Table

object DMsTable : Table("dms") {
    val id = varchar("id", 256)
    val name = varchar("name", 256)
    val userEmail = reference("user_email", UsersTable.email)

    override val primaryKey: PrimaryKey = PrimaryKey(id)
}

internal fun ResultRow.toDM(newMessageCount: Int) = DM.reconstruct(
    id = DMId(this[DMsTable.id]),
    name = DMName(this[DMsTable.name]),
    userEmail = Email(this[DMsTable.userEmail]),
    to = Avatar.reconstruct(Email(this[AvatarsTable.email])),
    newMessageCount = newMessageCount
)
