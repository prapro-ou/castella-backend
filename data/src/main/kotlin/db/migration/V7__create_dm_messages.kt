package db.migration

import db.table.DMMessagesTable
import org.flywaydb.core.api.migration.BaseJavaMigration
import org.flywaydb.core.api.migration.Context
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction

@Suppress("ClassName", "unused")
class V7__create_dm_messages : BaseJavaMigration() {
    override fun migrate(context: Context) {
        transaction { SchemaUtils.create(DMMessagesTable) }
    }
}
