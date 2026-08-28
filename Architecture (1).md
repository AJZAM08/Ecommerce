# Architecture — Katalog-Minyak (Mobile)
**Dokumen Arsitektur Teknis**
Versi: 0.1 (Draft Awal) | Tanggal: 28 Agustus 2026

---

## 1. Prinsip Arsitektur

1. **Backend Dipertahankan, Diperluas** — Flask app yang sudah ada tidak diganti; ditambah lapisan REST API (`/api/v1/...`) di atas `app.py`/`models.py` yang sudah ada, agar web (Jinja2) dan mobile bisa berbagi satu sumber data yang sama.
2. **Server sebagai Source of Truth** — harga, stok, dan status pesanan selalu divalidasi/ditentukan oleh server, bukan dipercaya mentah-mentah dari input aplikasi (krusial untuk e-commerce).
3. **Clean Architecture + MVVM** — sama seperti pola project Kotlin sebelumnya, agar konsisten dan mudah dikembangkan.
4. **Offline-friendly untuk Browsing, Online-required untuk Transaksi** — katalog produk bisa di-cache lokal, tapi checkout & pembayaran wajib online.

---

## 2. Tech Stack

| Layer | Teknologi |
|---|---|
| Bahasa (Mobile) | Kotlin |
| UI | Jetpack Compose |
| Arsitektur App | MVVM + Clean Architecture (Presentation → Domain → Data) |
| Dependency Injection | Hilt |
| Local Cache | Room (cache katalog produk, keranjang lokal sebelum checkout) |
| Secure Storage | DataStore + EncryptedSharedPreferences (token auth, tidak pernah simpan data kartu) |
| Networking | Retrofit + OkHttp (konsumsi REST API dari Flask) |
| Async | Kotlin Coroutines + Flow |
| Navigasi | Navigation Compose |
| Image Loading | Coil (untuk foto produk) |
| Payment | Midtrans Android SDK (Snap/Core API) |
| **Backend (existing, diperluas)** | Python Flask + Flask-SQLAlchemy (`models.py`) |
| Backend API Layer Tambahan | Flask Blueprint baru khusus JSON (mis. `api/routes.py`), response format JSON, auth via JWT (Flask-JWT-Extended) |
| Database Backend | SQLite (development, existing) → PostgreSQL (produksi, sesuai rencana awal proyek) |
| Hosting Backend | Render / Railway / PythonAnywhere (sesuai opsi yang sudah dipertimbangkan di dokumen proyek awal) |

---

## 3. Gambaran Arsitektur Tingkat Tinggi

```
┌─────────────────────────────────────────────────────────────┐
│                    Android App (Kotlin/Compose)                │
│  KatalogScreen | DetailScreen | CartScreen | CheckoutScreen   │
│  OrderHistoryScreen | ProfileScreen                            │
└───────────────────────────┬───────────────────────────────────┘
                             │ StateFlow / Events
┌───────────────────────────▼───────────────────────────────────┐
│                      ViewModel Layer                          │
│  ProductViewModel | CartViewModel | CheckoutViewModel | ...   │
└───────────────────────────┬───────────────────────────────────┘
                             │
┌───────────────────────────▼───────────────────────────────────┐
│                 Domain Layer (Use Cases)                      │
│ GetProductsUseCase | AddToCartUseCase | CheckoutUseCase |      │
│ ProcessPaymentUseCase | GetOrderStatusUseCase                 │
└───────┬───────────────────────────┬────────────────────────────┘
        │                           │
┌───────▼────────────┐   ┌──────────▼─────────────┐
│ ProductRepository    │   │ OrderRepository/         │
│ (Retrofit + Room     │   │ PaymentRepository         │
│  cache)               │   │ (Retrofit + Midtrans SDK)│
└───────┬────────────┘   └──────────┬─────────────┘
        │ HTTPS (REST/JSON)          │ HTTPS + Midtrans callback
        ▼                            ▼
┌─────────────────────────────────────────────────────────────┐
│              Backend Flask (existing, diperluas)               │
│  /api/v1/products  /api/v1/cart  /api/v1/orders  /api/v1/auth │
│  (Blueprint baru, di samping route HTML Jinja2 yang sudah ada) │
│                     models.py (SQLAlchemy)                     │
└───────────────────────────┬───────────────────────────────────┘
                             │
                    SQLite (dev) / PostgreSQL (prod)
```

---

## 4. Perubahan yang Dibutuhkan di Backend Flask (Existing)

Karena backend saat ini hanya melayani HTML (Jinja2), berikut penambahan yang dibutuhkan **tanpa mengubah route web yang sudah ada**:

| Penambahan | Detail |
|---|---|
| Blueprint API baru | Mis. `api_bp = Blueprint('api', __name__, url_prefix='/api/v1')`, didaftarkan terpisah dari route HTML |
| Endpoint Produk | `GET /api/v1/products`, `GET /api/v1/products/<id>`, dengan query param untuk filter kategori & pencarian |
| Endpoint Auth | `POST /api/v1/auth/register`, `POST /api/v1/auth/login` → mengembalikan JWT token |
| Endpoint Order | `POST /api/v1/orders` (checkout), `GET /api/v1/orders` (riwayat), `GET /api/v1/orders/<id>` (detail & status) |
| Endpoint Payment Callback | `POST /api/v1/payment/callback` — menerima notifikasi status dari Midtrans (webhook), memvalidasi signature, update status order |
| Serialisasi JSON | Tambahkan method `to_dict()` di tiap model (`Product`, `Order`, dst.) di `models.py`, atau gunakan library serializer (mis. Marshmallow) |
| CORS (jika perlu) | Umumnya tidak dibutuhkan untuk native Android (bukan browser), tapi tetap aktifkan jika ada kebutuhan testing via web client |

> **Catatan:** Admin panel (route HTML `/admin/...`) tetap seperti semula, tidak tersentuh — hanya berbagi `models.py` yang sama dengan API baru.

---

## 5. Alur Utama: Checkout & Pembayaran (End-to-End)

1. **Isi Keranjang** — `CartViewModel` menyimpan item keranjang di Room (lokal), belum terikat ke server sampai checkout.
2. **Checkout** — Pengguna isi alamat pengiriman, tekan "Buat Pesanan". `CheckoutUseCase` mengirim `POST /api/v1/orders` berisi daftar item + alamat.
3. **Validasi Server** — Backend memvalidasi ulang stok & harga tiap item (**tidak** memakai harga yang dikirim dari app — lihat `Rules.md`), menghitung ongkir, membuat record `Order` berstatus `pending_payment`.
4. **Inisiasi Pembayaran** — Backend membuat transaksi Midtrans (Snap Token/Core API), token dikembalikan ke app.
5. **Pembayaran** — App membuka Midtrans SDK (Snap UI) menggunakan token tersebut; pengguna memilih metode (transfer/e-wallet/dll) dan menyelesaikan pembayaran.
6. **Callback/Webhook** — Midtrans mengirim notifikasi ke `POST /api/v1/payment/callback` di backend; backend memvalidasi signature, lalu update status `Order` menjadi `Diproses` (jika sukses) atau `failed`.
7. **Polling/Refresh Status** — App melakukan refresh (`GET /api/v1/orders/<id>`) untuk menampilkan status terbaru ke pengguna (untuk MVP; push notification masuk fase lanjutan).

---

## 6. Caching & Mode Offline

- `ProductRepository` menerapkan pola **cache-then-network**: tampilkan data dari Room dulu (jika ada), lalu fetch dari API dan update cache + UI.
- Cart disimpan di Room secara lokal sampai checkout benar-benar dikirim ke server (menghindari data keranjang hilang saat app ditutup).
- Checkout, pembayaran, dan lihat status pesanan **wajib online** — ditampilkan pesan jelas jika tidak ada koneksi.

---

## 7. Keamanan Arsitektural (ringkas — detail lengkap di `Rules.md`)

- Autentikasi via JWT, disimpan di `EncryptedSharedPreferences`/DataStore terenkripsi, dikirim via header `Authorization: Bearer <token>`.
- Seluruh komunikasi API wajib HTTPS.
- Validasi harga/stok/total pembayaran **selalu di server**, app hanya mengirim `productId` + `quantity`, bukan harga.
- Webhook Midtrans wajib divalidasi signature-nya di backend sebelum mengubah status order (mencegah pemalsuan notifikasi pembayaran).
- Tidak ada data kartu pembayaran yang pernah disimpan di app maupun backend — seluruhnya ditangani oleh Midtrans (PCI-DSS compliant di pihak mereka).

---

## 8. Skalabilitas ke Fase Lanjutan

- **Push Notification**: tinggal tambah Firebase Cloud Messaging di app + trigger dari backend saat status order berubah, tanpa mengubah struktur inti.
- **Wishlist/Rating**: entitas baru di `models.py` + endpoint baru, tidak mengubah arsitektur inti.
- **Multi-alamat/metode pembayaran tersimpan**: perluasan tabel `Address`/`PaymentMethod`, domain layer sudah cukup generik untuk menampung ini.
