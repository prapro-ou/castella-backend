package db.seeding

import db.table.DMsAvatarsTable
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.batchInsert
import org.jetbrains.exposed.sql.transactions.transaction
import repository.db.seeding.DMsTableSeeder
import repository.db.seeding.DatabaseSeeder

object DMsAvatarsTableSeeder : DatabaseSeeder {
    override fun seeding(database: Database) {
        transaction(database) {
            DMsAvatarsTable.batchInsert(dmsAvatarsData) {
                this[DMsAvatarsTable.id] = it.hashCode().toString()
                this[DMsAvatarsTable.dmId] = it.first.id.value
                this[DMsAvatarsTable.avatarEmail] = it.second.email.value
            }
        }
    }

    private val dmsAvatarsData = DMsTableSeeder.dmData.zip(AvatarsTableSeeder.avatarsData)
}