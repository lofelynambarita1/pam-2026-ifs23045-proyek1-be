package org.delcom.entities

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class Layanan(
    var id: String = UUID.randomUUID().toString(),
    var nama: String,
    var pathGambar: String,
    var kategori: String,
    var deskripsi: String,
    var harga: Double,
    var durasiMenit: Int,
    var tersedia: Boolean = true,

    @Contextual
    val createdAt: Instant = Clock.System.now(),
    @Contextual
    var updatedAt: Instant = Clock.System.now(),
)
