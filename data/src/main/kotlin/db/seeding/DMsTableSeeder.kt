package repository.db.seeding

import avatar.Avatar
import destination.Destination
import destination.DestinationId
import destination.DestinationName
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.batchInsert
import org.jetbrains.exposed.sql.transactions.transaction
import repository.table.DMsTable
import user.Email

class DMsTableSeeder : DatabaseSeeder {
    override fun seeding(database: Database) {
        transaction(database) {
            DMsTable.batchInsert(dmData) {
                this[DMsTable.id] = it.id.value
                this[DMsTable.name] = it.name.value
                this[DMsTable.userEmail] = userData.first().email.value
            }
        }
    }

    private val dmData: List<Destination> = List(5) {index ->
        Destination.DM(
            id = DestinationId("DestinationId$index"),
            name = DestinationName("DestinationName$index"),

            // 使用しない
            to = Avatar(Email("")),
        )
    }
}