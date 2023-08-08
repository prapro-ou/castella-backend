package repository.db.seeding

import org.jetbrains.exposed.sql.Database

interface DatabaseSeeder {
    fun seeding(database: Database)
}
