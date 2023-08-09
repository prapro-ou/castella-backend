package db.migration

import db.table.GroupMessagesTable
import org.flywaydb.core.api.migration.BaseJavaMigration
import org.flywaydb.core.api.migration.Context
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction

@Suppress("ClassName", "unused")
class V8__create_group_messages : BaseJavaMigration() {
    override fun migrate(context: Context) {
        transaction { SchemaUtils.create(GroupMessagesTable) }
    }
}
