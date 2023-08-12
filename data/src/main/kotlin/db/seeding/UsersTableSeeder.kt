package db.seeding

import com.vb4.Email
import com.vb4.user.User
import db.table.UsersTable
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.batchInsert
import org.jetbrains.exposed.sql.transactions.transaction
import repository.db.seeding.DatabaseSeeder

object UsersTableSeeder : DatabaseSeeder {
    override fun seeding(database: Database) {
        transaction(database) {
            UsersTable.batchInsert(userData) {
                this[UsersTable.email] = it.email.value
            }
        }
    }

    internal val userData = listOf(
        User.reconstruct(email = Email("sample1@example.com"),),
        User.reconstruct(email = Email("inputUserEmail"),),
    )
}
