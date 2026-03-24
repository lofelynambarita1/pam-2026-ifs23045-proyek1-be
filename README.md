# hairlogy-be

Backend API untuk aplikasi **Hairlogy** — platform manajemen layanan salon rambut.

Dibangun menggunakan [Ktor](https://ktor.io) + Kotlin, dengan database PostgreSQL.

---

## Fitur

| Nama | Deskripsi |
|------|-----------|
| Routing | Mendefinisikan route terstruktur untuk setiap endpoint |
| Content Negotiation | Serialisasi/deserialisasi JSON otomatis |
| CORS | Mengizinkan akses dari semua host |
| Koin DI | Dependency Injection |
| Exposed ORM | Akses database dengan DSL & DAO |
| Dotenv | Konfigurasi melalui file `.env` |

---

## Struktur Endpoint

### Profile
| Method | Endpoint | Deskripsi |
|--------|----------|-----------|
| GET | `/profile` | Mengambil data profile pengembang |
| GET | `/profile/photo` | Mengambil foto profile |

### Layanan
| Method | Endpoint | Deskripsi |
|--------|----------|-----------|
| GET | `/layanan` | Mengambil semua data layanan (support query: `search`, `kategori`, `tersedia`) |
| POST | `/layanan` | Menambahkan layanan baru (multipart form) |
| GET | `/layanan/{id}` | Mengambil detail layanan berdasarkan ID |
| PUT | `/layanan/{id}` | Mengubah data layanan (multipart form) |
| DELETE | `/layanan/{id}` | Menghapus data layanan |
| GET | `/layanan/{id}/gambar` | Mengambil gambar layanan |

---

## Kategori Layanan yang Valid

- `Potong Rambut`
- `Pewarnaan`
- `Perawatan`
- `Styling`
- `Keriting`
- `Lainnya`

---

## Setup & Konfigurasi

### 1. Salin file environment

```bash
cp .env.example .env
```

Isi sesuai konfigurasi lokal:

```env
APP_HOST=localhost
APP_PORT=8000

DB_HOST=127.0.0.1
DB_PORT=5432
DB_NAME=db_hairlogy
DB_USER=postgres
DB_PASSWORD=postgres
```

### 2. Buat database PostgreSQL

```sql
CREATE DATABASE db_hairlogy;
```

Jalankan script SQL:

```bash
psql -U postgres -d db_hairlogy -f data.sql
```

---

## Build & Run

| Task | Deskripsi |
|------|-----------|
| `./gradlew test` | Menjalankan tes |
| `./gradlew build` | Build project |
| `./gradlew buildFatJar` | Build JAR dengan semua dependensi |
| `./gradlew run` | Menjalankan server |
| `./gradlew runDocker` | Menjalankan dengan Docker |

Jika server berhasil berjalan:

```
2024-12-04 14:32:45.584 [main] INFO  Application - Application started in 0.303 seconds.
2024-12-04 14:32:45.682 [main] INFO  Application - Responding at http://0.0.0.0:8000
```

---

## Struktur Proyek

```
src/main/kotlin/
├── Application.kt          # Entry point
├── Routing.kt              # Konfigurasi routing
├── dao/
│   └── LayananDAO.kt       # DAO Exposed
├── data/
│   ├── AppException.kt
│   ├── DataResponse.kt
│   ├── ErrorResponse.kt
│   └── LayananRequest.kt
├── entities/
│   └── Layanan.kt          # Model data
├── helpers/
│   ├── DatabaseHelper.kt
│   ├── MappingHelper.kt
│   ├── ToolsHelper.kt
│   └── ValidatorHelper.kt
├── module/
│   └── AppModule.kt        # Koin DI
├── repositories/
│   ├── ILayananRepository.kt
│   └── LayananRepository.kt
├── services/
│   ├── LayananService.kt
│   └── ProfileService.kt
└── tables/
    └── LayananTable.kt     # Skema tabel Exposed
```
