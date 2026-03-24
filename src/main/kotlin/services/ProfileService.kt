package org.delcom.services

import io.ktor.http.HttpStatusCode
import io.ktor.server.application.*
import io.ktor.server.response.*
import org.delcom.data.DataResponse
import java.io.File

class ProfileService {

    // Mengambil data profile pengembang
    suspend fun getProfile(call: ApplicationCall) {
        val response = DataResponse(
            "success",
            "Berhasil mengambil profile pengembang",
            mapOf(
                Pair("username", "Lofelyn.Ambarita"),
                Pair("nama", "Lofelyn Enzely Ambarita"),
                Pair(
                    "tentang",
                    "Saya adalah pengembang aplikasi Hairlogy, platform manajemen layanan salon rambut " +
                    "yang memudahkan pelanggan menemukan dan memesan layanan perawatan rambut terbaik."
                ),
            )
        )
        call.respond(response)
    }

    // Mengambil photo profile
    suspend fun getProfilePhoto(call: ApplicationCall) {
        val file = File("uploads/profile/me.png")

        if (!file.exists()) {
            return call.respond(HttpStatusCode.NotFound)
        }

        call.respondFile(file)
    }
}
