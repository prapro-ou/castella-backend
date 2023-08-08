package db

import org.flywaydb.core.Flyway
import javax.sql.DataSource

fun DataSource.migrate() {
    val flyway = Flyway.configure()
        .dataSource(this)
        .load()

    flyway.info()
    flyway.migrate()
}
