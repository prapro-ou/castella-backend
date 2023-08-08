package db.seeding

import com.vb4.group.Group
import com.vb4.group.GroupId
import com.vb4.group.GroupName
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.batchInsert
import org.jetbrains.exposed.sql.transactions.transaction
import db.table.GroupsTable
import repository.db.seeding.DatabaseSeeder
import repository.db.seeding.UsersTableSeeder

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

    val groupData: List<Group> = List(5) { index ->
        Group(
            id = GroupId("GroupId$index"),
            name = GroupName("GroupName$index"),

            // 使用しない
            to = listOf(),
        )
    }
}