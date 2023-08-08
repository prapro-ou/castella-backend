package repository.db.seeding

import destination.Destination
import destination.DestinationId
import destination.DestinationName
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.batchInsert
import org.jetbrains.exposed.sql.transactions.transaction
import table.GroupsTable

object GroupsTableSeeder : DatabaseSeeder {
    override fun seeding(database: Database) {
        transaction(database) {
            GroupsTable.batchInsert(groupData) {
                this[GroupsTable.id] = it.id.value
                this[GroupsTable.name] = it.name.value
                this[GroupsTable.userEmail] = UsersTableSeeder.userData.first().email.value
            }
        }
    }

    private val groupData: List<Destination> = List(5) {index ->
        Destination.Group(
            id = DestinationId("DestinationId$index"),
            name = DestinationName("DestinationName$index"),

            // 使用しない
            to = listOf(),
        )
    }
}