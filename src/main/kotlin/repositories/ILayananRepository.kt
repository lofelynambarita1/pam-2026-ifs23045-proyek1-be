package org.delcom.repositories

import org.delcom.entities.Layanan

interface ILayananRepository {
    suspend fun getLayanan(search: String, kategori: String, tersedia: String): List<Layanan>
    suspend fun getLayananById(id: String): Layanan?
    suspend fun getLayananByNama(nama: String): Layanan?
    suspend fun addLayanan(layanan: Layanan): String
    suspend fun updateLayanan(id: String, newLayanan: Layanan): Boolean
    suspend fun removeLayanan(id: String): Boolean
}
