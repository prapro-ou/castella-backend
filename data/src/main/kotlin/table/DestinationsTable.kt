package repository.table

import org.jetbrains.exposed.sql.Table

object DestinationsTable : Table("destinations") {
    val id = varchar("id", 256)
    val name = varchar("name", 256)
    val userEmail = varchar("user_email", 256)
}