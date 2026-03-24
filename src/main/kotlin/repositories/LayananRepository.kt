package org.delcom.repositories

import org.delcom.dao.LayananDAO
import org.delcom.entities.Layanan
import org.delcom.helpers.daoToModel
import org.delcom.helpers.suspendTransaction
import org.delcom.tables.LayananTable
import org.jetbrains.exposed.sql.SortOrder
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.andWhere
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.lowerCase
import org.jetbrains.exposed.sql.selectAll
import java.util.UUID

class LayananRepository : ILayananRepository {

    override suspend fun getLayanan(
        search: String,
        kategori: String,
        tersedia: String
    ): List<Layanan> = suspendTransaction {
        var query = LayananTable.selectAll()

        if (search.isNotBlank()) {
            val keyword = "%${search.lowercase()}%"
            query = query.andWhere { LayananTable.nama.lowerCase() like keyword }
        }
        if (kategori.isNotBlank()) {
            query = query.andWhere {
                LayananTable.kategori.lowerCase() like "%${kategori.lowercase()}%"
            }
        }
        if (tersedia.isNotBlank()) {
            val isTersedia = tersedia.lowercase() == "true"
            query = query.andWhere { LayananTable.tersedia eq isTersedia }
        }

        query.orderBy(LayananTable.createdAt to SortOrder.DESC)
            .limit(20)
            .map { row ->
                Layanan(
                    id          = row[LayananTable.id].value.toString(),
                    nama        = row[LayananTable.nama],
                    pathGambar  = row[LayananTable.pathGambar],
                    kategori    = row[LayananTable.kategori],
                    deskripsi   = row[LayananTable.deskripsi],
                    harga       = row[LayananTable.harga].toDouble(),
                    durasiMenit = row[LayananTable.durasiMenit],
                    tersedia    = row[LayananTable.tersedia],
                    createdAt   = row[LayananTable.createdAt],
                    updatedAt   = row[LayananTable.updatedAt],
                )
            }
    }

    override suspend fun getLayananById(id: String): Layanan? = suspendTransaction {
        LayananDAO
            .find { LayananTable.id eq UUID.fromString(id) }
            .limit(1)
            .map(::daoToModel)
            .firstOrNull()
    }

    override suspend fun getLayananByNama(nama: String): Layanan? = suspendTransaction {
        LayananDAO
            .find { LayananTable.nama eq nama }
            .limit(1)
            .map(::daoToModel)
            .firstOrNull()
    }

    override suspend fun addLayanan(layanan: Layanan): String = suspendTransaction {
        val dao = LayananDAO.new {
            nama        = layanan.nama
            pathGambar  = layanan.pathGambar
            kategori    = layanan.kategori
            deskripsi   = layanan.deskripsi
            harga       = layanan.harga.toBigDecimal()
            durasiMenit = layanan.durasiMenit
            tersedia    = layanan.tersedia
            createdAt   = layanan.createdAt
            updatedAt   = layanan.updatedAt
        }
        dao.id.value.toString()
    }

    override suspend fun updateLayanan(id: String, newLayanan: Layanan): Boolean =
        suspendTransaction {
            val dao = LayananDAO
                .find { LayananTable.id eq UUID.fromString(id) }
                .limit(1)
                .firstOrNull()

            if (dao != null) {
                dao.nama        = newLayanan.nama
                dao.pathGambar  = newLayanan.pathGambar
                dao.kategori    = newLayanan.kategori
                dao.deskripsi   = newLayanan.deskripsi
                dao.harga       = newLayanan.harga.toBigDecimal()
                dao.durasiMenit = newLayanan.durasiMenit
                dao.tersedia    = newLayanan.tersedia
                dao.updatedAt   = newLayanan.updatedAt
                true
            } else {
                false
            }
        }

    override suspend fun removeLayanan(id: String): Boolean = suspendTransaction {
        val rowsDeleted = LayananTable.deleteWhere {
            LayananTable.id eq UUID.fromString(id)
        }
        rowsDeleted == 1
    }
}
