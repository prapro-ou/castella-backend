package repository.db.seeding

import com.vb4.avatar.Avatar
import com.vb4.destination.Destination
import com.vb4.destination.DestinationId
import com.vb4.destination.DestinationName
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.batchInsert
import org.jetbrains.exposed.sql.transactions.transaction
import db.table.DMsTable
import com.vb4.user.Email

object DMsTableSeeder : DatabaseSeeder {
    override fun seeding(database: Database) {
        transaction(database) {
            DMsTable.batchInsert(dmData) {
                this[DMsTable.id] = it.id.value
                this[DMsTable.name] = it.name.value
                this[DMsTable.userEmail] = UsersTableSeeder.userData.first().email.value
            }
        }
    }

    val dmData: List<Destination> = List(5) { index ->
        Destination.DM(
            id = DestinationId("DestinationId$index"),
            name = DestinationName("DestinationName$index"),

            // 使用しない
            to = Avatar(Email("")),
        )
    }
}