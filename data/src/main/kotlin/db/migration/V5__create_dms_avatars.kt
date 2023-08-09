package db.migration

import db.table.DMsAvatarsTable
import org.flywaydb.core.api.migration.BaseJavaMigration
import org.flywaydb.core.api.migration.Context
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction

@Suppress("ClassName", "unused")
class V5__create_dms_avatars : BaseJavaMigration() {
    override fun migrate(context: Context) {
        transaction { SchemaUtils.create(DMsAvatarsTable) }
    }
}
