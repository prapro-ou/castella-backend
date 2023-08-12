package db.seeding

import com.vb4.Email
import com.vb4.avatar.Avatar
import db.table.AvatarsTable
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.batchInsert
import org.jetbrains.exposed.sql.transactions.transaction
import repository.db.seeding.DatabaseSeeder

object AvatarsTableSeeder : DatabaseSeeder {
    override fun seeding(database: Database) {
        transaction(database) {
            AvatarsTable.batchInsert(avatarsData) {
                this[AvatarsTable.email] = it.email.value
            }
        }
    }

    val avatarsData: List<Avatar> = List(5) { index ->
        Avatar.reconstruct(Email("sample$index@example.com"))
    } + Avatar.reconstruct(Email("notifications@github.com"))
}
