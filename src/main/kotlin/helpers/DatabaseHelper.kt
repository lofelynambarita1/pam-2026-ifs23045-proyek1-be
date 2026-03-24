package org.delcom.helpers

import io.github.cdimascio.dotenv.dotenv
import io.ktor.server.application.*
import org.jetbrains.exposed.sql.Database

fun Application.configureDatabases() {
    // Baca dari dotenv langsung, bukan dari ktor config
    // Karena kita tidak pakai EngineMain, application.yaml tidak dipakai
    val dotenv = dotenv {
        directory = "."
        ignoreIfMissing = true
    }

    val dbHost     = dotenv["DB_HOST",     "127.0.0.1"]
    val dbPort     = dotenv["DB_PORT",     "5432"]
    val dbName     = dotenv["DB_NAME",     "db_hairlogy"]
    val dbUser     = dotenv["DB_USER",     "postgres"]
    val dbPassword = dotenv["DB_PASSWORD", "postgres"]

    Database.connect(
        url      = "jdbc:postgresql://$dbHost:$dbPort/$dbName",
        user     = dbUser,
        password = dbPassword
    )
}