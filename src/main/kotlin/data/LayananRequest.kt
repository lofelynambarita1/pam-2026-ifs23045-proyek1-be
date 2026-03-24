package org.delcom.data

import kotlinx.serialization.Serializable
import org.delcom.entities.Layanan

@Serializable
data class LayananRequest(
    var nama: String = "",
    var kategori: String = "",
    var deskripsi: String = "",
    var harga: Double = 0.0,
    var durasiMenit: Int = 30,
    var tersedia: Boolean = true,
    var pathGambar: String = "",
) {
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "nama"        to nama,
            "kategori"    to kategori,
            "deskripsi"   to deskripsi,
            "harga"       to harga,
            "durasiMenit" to durasiMenit,
            "tersedia"    to tersedia,
            "pathGambar"  to pathGambar,
        )
    }

    fun toEntity(): Layanan {
        return Layanan(
            nama        = nama,
            kategori    = kategori,
            deskripsi   = deskripsi,
            harga       = harga,
            durasiMenit = durasiMenit,
            tersedia    = tersedia,
            pathGambar  = pathGambar,
        )
    }
}
