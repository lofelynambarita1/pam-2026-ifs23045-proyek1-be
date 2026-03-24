package org.delcom.helpers

import kotlinx.coroutines.Dispatchers
import org.delcom.dao.LayananDAO
import org.delcom.entities.Layanan
import org.jetbrains.exposed.sql.Transaction
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

suspend fun <T> suspendTransaction(block: Transaction.() -> T): T =
    newSuspendedTransaction(Dispatchers.IO, statement = block)

fun daoToModel(dao: LayananDAO) = Layanan(
    id          = dao.id.value.toString(),
    nama        = dao.nama,
    pathGambar  = dao.pathGambar,
    kategori    = dao.kategori,
    deskripsi   = dao.deskripsi,
    harga       = dao.harga.toDouble(),
    durasiMenit = dao.durasiMenit,
    tersedia    = dao.tersedia,
    createdAt   = dao.createdAt,
    updatedAt   = dao.updatedAt,
)
