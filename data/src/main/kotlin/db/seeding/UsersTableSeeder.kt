package repository.db.seeding

import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.batchInsert
import org.jetbrains.exposed.sql.transactions.transaction
import db.table.UsersTable
import com.vb4.Email
import com.vb4.user.User

object UsersTableSeeder : DatabaseSeeder {
    override fun seeding(database: Database) {
        transaction(database) {
            UsersTable.batchInsert(userData) {
                this[UsersTable.email] = it.email.value
            }
        }
    }

    internal val userData = listOf(
        User(
            email = Email("sample1@example.com"),

            // 使用しない
            dms = listOf(),
            groups = listOf(),
        )
    )

}
