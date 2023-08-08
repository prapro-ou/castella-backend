package db.migration

import org.flywaydb.core.api.migration.BaseJavaMigration
import org.flywaydb.core.api.migration.Context
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import db.table.GroupsTable

@Suppress("ClassName", "unused")
class V3__create_groups : BaseJavaMigration() {
    override fun migrate(context: Context) {
        transaction { SchemaUtils.create(GroupsTable) }
    }
}
