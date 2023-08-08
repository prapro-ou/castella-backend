package db.table

import org.jetbrains.exposed.sql.Table

object DMsTable : Table("dms") {
    val id = varchar("id", 256)
    val name = varchar("name", 256)
    val userEmail = reference("user_email", UsersTable.email)

    override val primaryKey: PrimaryKey = PrimaryKey(id)
}
