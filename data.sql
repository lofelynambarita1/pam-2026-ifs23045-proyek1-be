CREATE TABLE IF NOT EXISTS layanan (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    nama VARCHAR(200) NOT NULL,
    path_gambar VARCHAR(255) NOT NULL DEFAULT '',
    kategori VARCHAR(100) NOT NULL,
    deskripsi TEXT NOT NULL,
    harga DECIMAL(12, 2) NOT NULL DEFAULT 0.00,
    durasi_menit INTEGER NOT NULL DEFAULT 30,
    tersedia BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);
