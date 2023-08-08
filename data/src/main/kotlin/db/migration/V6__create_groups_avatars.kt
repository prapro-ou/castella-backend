package db.migration

import org.flywaydb.core.api.migration.BaseJavaMigration
import org.flywaydb.core.api.migration.Context
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import table.GroupsAvatarsTable

@Suppress("ClassName", "unused")
class V6__create_groups_avatars : BaseJavaMigration() {
    override fun migrate(context: Context) {
        transaction { SchemaUtils.create(GroupsAvatarsTable) }
    }
}