package db.seeding

import com.vb4.Email
import com.vb4.avatar.Avatar
import com.vb4.dm.DM
import com.vb4.dm.DMId
import com.vb4.dm.DMName
import db.table.DMsTable
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.batchInsert
import org.jetbrains.exposed.sql.transactions.transaction
import repository.db.seeding.DatabaseSeeder

object DMsTableSeeder : DatabaseSeeder {
    override fun seeding(database: Database) {
        transaction(database) {
            DMsTable.batchInsert(dmData) {
                this[DMsTable.id] = it.id.value
                this[DMsTable.name] = it.name.value
                this[DMsTable.userEmail] = it.userEmail.value
            }
        }
    }

    val dmData: List<DM> = List(5) { index ->
        DM(
            id = DMId("DMId$index"),
            name = DMName("DMName$index"),
            userEmail = Email("sample1@example.com"),

            // 使用しない
            to = Avatar(Email("")),
        )
    } + DM(
        id = DMId("example"),
        name = DMName("中銀"),
        userEmail = Email("inputUserEmail"),
        to = Avatar(Email("notifications@github.com")),
    )
}
