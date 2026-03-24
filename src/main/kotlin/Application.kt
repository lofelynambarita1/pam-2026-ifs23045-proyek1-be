package org.delcom

import io.github.cdimascio.dotenv.dotenv
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.cors.routing.*
import kotlinx.serialization.json.Json
import org.delcom.helpers.configureDatabases
import org.delcom.module.appModule
import org.koin.ktor.plugin.Koin
import org.koin.logger.slf4jLogger

fun main(args: Array<String>) {
    // 1. Load .env lebih dulu sebelum apapun
    val dotenv = dotenv {
        directory = "."
        ignoreIfMissing = true
    }

    // 2. Ambil nilai konfigurasi dari .env atau gunakan default
    val host = dotenv["APP_HOST", "localhost"]
    val port = dotenv["APP_PORT", "8000"].toInt()

    // 3. Start server secara programatik — TIDAK pakai EngineMain
    //    Ini menghindari Ktor mem-parse application.yaml via System.getenv()
    //    sebelum dotenv sempat di-load
    embeddedServer(Netty, port = port, host = host) {
        module()
    }.start(wait = true)
}

fun Application.module() {
    install(CORS) {
        anyHost()
        allowHeader("Content-Type")
        allowHeader("Authorization")
    }

    install(ContentNegotiation) {
        json(Json {
            explicitNulls = false
            prettyPrint = false
            ignoreUnknownKeys = true
            isLenient = true
        })
    }

    install(Koin) {
        slf4jLogger()
        modules(appModule)
    }

    configureDatabases()
    configureRouting()
}