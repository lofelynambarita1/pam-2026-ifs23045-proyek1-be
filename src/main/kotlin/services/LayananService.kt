package org.delcom.services

import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.util.cio.*
import io.ktor.utils.io.*
import org.delcom.data.AppException
import org.delcom.data.DataResponse
import org.delcom.data.LayananRequest
import org.delcom.helpers.ValidatorHelper
import org.delcom.repositories.ILayananRepository
import java.io.File
import java.util.UUID

class LayananService(private val layananRepository: ILayananRepository) {

    // Mengambil semua data layanan
    suspend fun getAllLayanan(call: ApplicationCall) {
        val search   = call.request.queryParameters["search"]   ?: ""
        val kategori = call.request.queryParameters["kategori"] ?: ""
        val tersedia = call.request.queryParameters["tersedia"] ?: ""

        val layananList = layananRepository.getLayanan(search, kategori, tersedia)

        call.respond(
            DataResponse(
                status  = "success",
                message = "Berhasil mengambil daftar layanan",
                data    = mapOf("layanan" to layananList)
            )
        )
    }

    // Mengambil data layanan berdasarkan id
    suspend fun getLayananById(call: ApplicationCall) {
        val id = call.parameters["id"]
            ?: throw AppException(400, "ID layanan tidak boleh kosong!")

        val layanan = layananRepository.getLayananById(id)
            ?: throw AppException(404, "Data layanan tidak tersedia!")

        call.respond(
            DataResponse(
                status  = "success",
                message = "Berhasil mengambil data layanan",
                data    = mapOf("layanan" to layanan)
            )
        )
    }

    // Ambil data request dari multipart form
    private suspend fun getLayananRequest(call: ApplicationCall): LayananRequest {
        val req = LayananRequest()

        val multipartData = call.receiveMultipart(formFieldLimit = 1024 * 1024 * 5)
        multipartData.forEachPart { part ->
            when (part) {
                is PartData.FormItem -> {
                    when (part.name) {
                        "nama"        -> req.nama        = part.value.trim()
                        "kategori"    -> req.kategori    = part.value.trim()
                        "deskripsi"   -> req.deskripsi   = part.value
                        "harga"       -> req.harga       = part.value.trim().toDoubleOrNull() ?: 0.0
                        "durasiMenit" -> req.durasiMenit = part.value.trim().toIntOrNull() ?: 30
                        "tersedia"    -> req.tersedia    = part.value.trim().lowercase() == "true"
                    }
                }

                is PartData.FileItem -> {
                    val ext = part.originalFileName
                        ?.substringAfterLast('.', "")
                        ?.let { if (it.isNotEmpty()) ".$it" else "" }
                        ?: ""

                    val fileName = UUID.randomUUID().toString() + ext
                    val filePath = "uploads/layanan/$fileName"

                    val file = File(filePath)
                    file.parentFile.mkdirs()

                    part.provider().copyAndClose(file.writeChannel())
                    req.pathGambar = filePath
                }

                else -> {}
            }
            part.dispose()
        }

        return req
    }

    // Validasi request
    private fun validateLayananRequest(req: LayananRequest) {
        val v = ValidatorHelper(req.toMap())
        v.required("nama",       "Nama layanan tidak boleh kosong")
        v.required("kategori",   "Kategori tidak boleh kosong")
        v.required("deskripsi",  "Deskripsi tidak boleh kosong")
        v.required("pathGambar", "Gambar layanan tidak boleh kosong")
        v.minDouble("harga",    0.0, "Harga tidak boleh negatif")
        v.minInt("durasiMenit",   1, "Durasi minimal 1 menit")
        v.inList(
            "kategori",
            listOf("Potong Rambut", "Pewarnaan", "Perawatan", "Styling", "Keriting", "Lainnya"),
            "Kategori harus salah satu dari: Potong Rambut, Pewarnaan, Perawatan, Styling, Keriting, Lainnya"
        )
        v.validate()

        val file = File(req.pathGambar)
        if (!file.exists()) {
            throw AppException(400, "Gambar layanan gagal diupload!")
        }
    }

    // Menambahkan data layanan
    suspend fun createLayanan(call: ApplicationCall) {
        val req = getLayananRequest(call)

        validateLayananRequest(req)

        // Cek nama duplikat
        val exist = layananRepository.getLayananByNama(req.nama)
        if (exist != null) {
            File(req.pathGambar).takeIf { it.exists() }?.delete()
            throw AppException(409, "Layanan dengan nama ini sudah terdaftar!")
        }

        val layananId = layananRepository.addLayanan(req.toEntity())

        call.respond(
            DataResponse(
                status  = "success",
                message = "Berhasil menambahkan data layanan",
                data    = mapOf("layananId" to layananId)
            )
        )
    }

    // Mengubah data layanan
    suspend fun updateLayanan(call: ApplicationCall) {
        val id = call.parameters["id"]
            ?: throw AppException(400, "ID layanan tidak boleh kosong!")

        val oldLayanan = layananRepository.getLayananById(id)
            ?: throw AppException(404, "Data layanan tidak tersedia!")

        val req = getLayananRequest(call)

        // Jika tidak upload gambar baru, pakai yang lama
        if (req.pathGambar.isEmpty()) {
            req.pathGambar = oldLayanan.pathGambar
        }

        validateLayananRequest(req)

        // Cek nama duplikat jika nama diubah
        if (req.nama != oldLayanan.nama) {
            val exist = layananRepository.getLayananByNama(req.nama)
            if (exist != null) {
                File(req.pathGambar).takeIf { it.exists() }?.delete()
                throw AppException(409, "Layanan dengan nama ini sudah terdaftar!")
            }
        }

        // Hapus gambar lama jika ada gambar baru
        if (req.pathGambar != oldLayanan.pathGambar) {
            File(oldLayanan.pathGambar).takeIf { it.exists() }?.delete()
        }

        val isUpdated = layananRepository.updateLayanan(id, req.toEntity())
        if (!isUpdated) {
            throw AppException(400, "Gagal memperbarui data layanan!")
        }

        call.respond(
            DataResponse<Nothing>(
                status  = "success",
                message = "Berhasil mengubah data layanan",
                data    = null
            )
        )
    }

    // Menghapus data layanan
    suspend fun deleteLayanan(call: ApplicationCall) {
        val id = call.parameters["id"]
            ?: throw AppException(400, "ID layanan tidak boleh kosong!")

        val oldLayanan = layananRepository.getLayananById(id)
            ?: throw AppException(404, "Data layanan tidak tersedia!")

        val oldFile = File(oldLayanan.pathGambar)

        val isDeleted = layananRepository.removeLayanan(id)
        if (!isDeleted) {
            throw AppException(400, "Gagal menghapus data layanan!")
        }

        if (oldFile.exists()) oldFile.delete()

        call.respond(
            DataResponse<Nothing>(
                status  = "success",
                message = "Berhasil menghapus data layanan",
                data    = null
            )
        )
    }

    // Mengambil gambar layanan
    suspend fun getLayananGambar(call: ApplicationCall) {
        val id = call.parameters["id"]
            ?: return call.respond(HttpStatusCode.BadRequest)

        val layanan = layananRepository.getLayananById(id)
            ?: return call.respond(HttpStatusCode.NotFound)

        val file = File(layanan.pathGambar)
        if (!file.exists()) {
            return call.respond(HttpStatusCode.NotFound)
        }

        call.respondFile(file)
    }
}
