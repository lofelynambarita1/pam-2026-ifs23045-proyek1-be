package org.delcom.dao

import org.delcom.tables.LayananTable
import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.id.EntityID
import java.util.UUID

class LayananDAO(id: EntityID<UUID>) : Entity<UUID>(id) {
    companion object : EntityClass<UUID, LayananDAO>(LayananTable)

    var nama        by LayananTable.nama
    var pathGambar  by LayananTable.pathGambar
    var kategori    by LayananTable.kategori
    var deskripsi   by LayananTable.deskripsi
    var harga       by LayananTable.harga
    var durasiMenit by LayananTable.durasiMenit
    var tersedia    by LayananTable.tersedia
    var createdAt   by LayananTable.createdAt
    var updatedAt   by LayananTable.updatedAt
}
