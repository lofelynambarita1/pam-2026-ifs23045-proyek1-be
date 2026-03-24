package org.delcom.tables

import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.kotlin.datetime.timestamp

object LayananTable : UUIDTable("layanan") {
    val nama        = varchar("nama", 200)
    val pathGambar  = varchar("path_gambar", 255).default("")
    val kategori    = varchar("kategori", 100)
    val deskripsi   = text("deskripsi")
    val harga       = decimal("harga", 12, 2).default(0.0.toBigDecimal())
    val durasiMenit = integer("durasi_menit").default(30)
    val tersedia    = bool("tersedia").default(true)
    val createdAt   = timestamp("created_at")
    val updatedAt   = timestamp("updated_at")
}
