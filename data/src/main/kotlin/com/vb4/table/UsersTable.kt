package com.vb4.table

import org.jetbrains.exposed.sql.Table

object UsersTable : Table("users") {
    val email = varchar("email", 256)

    override val primaryKey: PrimaryKey = PrimaryKey(email)
}
