package db.migration

import db.table.UsersTable
import org.flywaydb.core.api.migration.BaseJavaMigration
import org.flywaydb.core.api.migration.Context
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction

@Suppress("ClassName", "unused")
class V1__create_users : BaseJavaMigration() {
    override fun migrate(context: Context) {
        transaction { SchemaUtils.create(UsersTable) }
    }
}
