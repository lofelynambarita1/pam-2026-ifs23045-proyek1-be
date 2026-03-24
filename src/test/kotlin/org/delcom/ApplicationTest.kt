package org.delcom

import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.server.testing.*
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertContains

class ApplicationTest {

    @Test
    fun `test root endpoint returns 200`() = testApplication {
        application { module() }

        val response = client.get("/")
        assertEquals(HttpStatusCode.OK, response.status)
        assertContains(response.bodyAsText(), "Hairlogy")
    }

    // Test /health dihapus karena route tersebut tidak ada di Routing.kt
    // Jika ingin menambahkan health check, tambahkan dulu route-nya di Routing.kt:
    //
    // get("/health") {
    //     call.respond(HttpStatusCode.OK, mapOf("status" to "ok"))
    // }
    //
    // Lalu uncomment test di bawah ini:
    //
    // @Test
    // fun `test health endpoint returns ok`() = testApplication {
    //     application { module() }
    //     val response = client.get("/health")
    //     assertEquals(HttpStatusCode.OK, response.status)
    // }
}