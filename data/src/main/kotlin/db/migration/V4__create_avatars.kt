package db.migration

import org.flywaydb.core.api.migration.BaseJavaMigration
import org.flywaydb.core.api.migration.Context
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import repository.table.AvatarsTable

@Suppress("ClassName", "unused")
class V4__create_avatars : BaseJavaMigration() {
    override fun migrate(context: Context) {
        transaction { SchemaUtils.create(AvatarsTable) }
    }
}
