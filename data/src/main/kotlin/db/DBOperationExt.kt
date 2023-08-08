package db

import org.flywaydb.core.Flyway
import javax.sql.DataSource
import org.jetbrains.exposed.sql.Database
import repository.db.seeding.DatabaseSeeder

fun DataSource.migrate() {
    val flyway = Flyway.configure()
        .dataSource(this)
        .load()

    flyway.info()
    flyway.migrate()
}

val seeding = listOf<DatabaseSeeder>()

fun Database.seeding() = seeding.forEach { it.seeding(database = this) }
