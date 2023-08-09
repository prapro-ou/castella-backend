package db

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import db.seeding.AvatarsTableSeeder
import db.seeding.DMsAvatarsTableSeeder
import db.seeding.DMsTableSeeder
import db.seeding.GroupsAvatarsTableSeeder
import db.seeding.GroupsTableSeeder
import db.seeding.UsersTableSeeder
import org.flywaydb.core.Flyway
import org.jetbrains.exposed.sql.Database
import javax.sql.DataSource

val DevDB: Database by lazy {
    val pool = hikari()
    Database.connect(pool)
        .also { pool.migrate() }
        .also { it.seeding() }
}

private fun hikari(): HikariDataSource {
    val config = HikariConfig().apply {
        driverClassName = "org.h2.Driver"
        jdbcUrl = "jdbc:h2:mem:dev_db;DB_CLOSE_DELAY=-1"
        username = ""
        password = ""
        maximumPoolSize = MAXIMUM_POOL_SIZE
        isAutoCommit = false
        validate()
    }
    return HikariDataSource(config)
}

private fun DataSource.migrate() {
    val flyway = Flyway.configure()
        .dataSource(this)
        .load()

    flyway.info()
    flyway.migrate()
}

fun Database.seeding() = listOf(
    UsersTableSeeder,
    DMsTableSeeder,
    GroupsTableSeeder,
    AvatarsTableSeeder,
    DMsAvatarsTableSeeder,
    GroupsAvatarsTableSeeder,
)
    .forEach { it.seeding(database = this) }

private const val MAXIMUM_POOL_SIZE = 3
