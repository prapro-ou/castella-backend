package db.seeding

import db.table.GroupsAvatarsTable
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.batchInsert
import org.jetbrains.exposed.sql.transactions.transaction
import repository.db.seeding.DatabaseSeeder

object GroupsAvatarsTableSeeder : DatabaseSeeder {
    override fun seeding(database: Database) {
        transaction(database) {
            GroupsAvatarsTable.batchInsert(groupsAvatarsData) {
                this[GroupsAvatarsTable.id] = it.hashCode().toString()
                this[GroupsAvatarsTable.groupId] = it.first.id.value
                this[GroupsAvatarsTable.avatarEmail] = it.second.email.value
            }
        }
    }

    private val groupsAvatarsData = AvatarsTableSeeder
        .avatarsData
        .map { GroupsTableSeeder.groupData.first() to it }
}