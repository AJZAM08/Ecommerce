# PRD — Katalog-Minyak (Mobile)
**Product Requirements Document**
Versi: 0.1 (Draft Awal) | Tanggal: 28 Agustus 2026 | Status: Discovery

---

## 1. Ringkasan Produk

**Katalog-Minyak (Mobile)** adalah aplikasi Android native berbasis Kotlin untuk platform e-commerce katalog rempah-rempah dan minyak atsiri, sebagai pendamping mobile dari proyek web yang sudah ada (backend Flask, repo `azzahrarachmatfatimah-cloud/Katalog-Minyak`). Aplikasi ini memungkinkan pengguna menjelajahi katalog produk, melakukan pemesanan, dan membayar langsung dari HP.

### 1.1 Latar Belakang
- Proyek web sudah memiliki backend Flask + database produk, namun berbasis server-rendered HTML (Jinja2) sehingga tidak bisa langsung dikonsumsi aplikasi mobile native.
- Backend Flask akan **diperluas** (bukan diganti) menjadi REST API berbasis JSON, agar bisa dipakai bersama oleh web (tetap jalan seperti biasa) dan app Android yang baru.
- Admin panel (manajemen produk & pesanan) **tetap di web** — app mobile ini fokus penuh untuk sisi pembeli (buyer-facing).

### 1.2 Visi
Memberikan pengalaman belanja produk rempah & minyak atsiri yang lebih cepat dan nyaman di HP, lengkap dengan pembayaran online langsung di aplikasi (tidak lagi form pemesanan manual seperti versi web awal).

---

## 2. Target Pengguna

| Segmen | Deskripsi |
|---|---|
| B2C | Konsumen akhir yang membeli produk rempah/minyak atsiri untuk kebutuhan pribadi |
| B2B | Pembeli grosir yang membutuhkan produk dalam jumlah lebih besar |

---

## 3. Masalah yang Diselesaikan

1. Versi web saat ini masih menggunakan form pemesanan manual tanpa pembayaran online — mempersulit transaksi cepat.
2. Belanja lewat browser mobile kurang nyaman dibanding aplikasi native (loading lebih lambat, tidak ada akses offline untuk lihat katalog, tidak ada notifikasi status pesanan).
3. Belum ada cara bagi pembeli untuk melacak status pesanan mereka secara real-time.

---

## 4. Ruang Lingkup Fitur

### 4.1 MVP (Fase 1)
| Fitur | Deskripsi | Prioritas |
|---|---|---|
| Autentikasi | Registrasi & login pengguna (email/password) | Must |
| Katalog Produk & Pencarian | Menampilkan daftar rempah & minyak atsiri dengan foto, deskripsi, varian ukuran, harga | Must |
| Filter & Kategori | Filter berdasarkan kategori (Minyak Murni, Rempah Kering, Ekstrak Herbal) + pencarian nama produk | Must |
| Detail Produk | Manfaat, komposisi, cara penggunaan, status stok | Must |
| Keranjang Belanja | Tambah/kurangi jumlah produk, hapus item | Must |
| Checkout | Form alamat pengiriman + estimasi ongkos kirim + ringkasan total harga | Must |
| Pembayaran Online | Integrasi payment gateway (Midtrans) — transfer bank, e-wallet, dll | Must |
| Riwayat & Status Pesanan | Melihat pesanan yang pernah dibuat & status terkini (Pending, Diproses, Dikirim, Selesai) | Must |
| Profil Pengguna | Kelola data diri, alamat tersimpan | Should |
| Mode Offline (Katalog) | Katalog produk tetap bisa dilihat (dari cache lokal) saat tanpa koneksi | Could |

### 4.2 Fase Lanjutan (Post-MVP)
| Fitur | Deskripsi |
|---|---|
| Notifikasi Push | Update status pesanan real-time via push notification |
| Wishlist / Favorit | Simpan produk untuk dibeli nanti |
| Rating & Ulasan Produk | Pembeli bisa memberi rating & review |
| Voucher/Diskon | Kode promo saat checkout |
| Multi-alamat & Multi-metode Pembayaran Tersimpan | Kemudahan checkout berulang |
| Live Chat / Customer Support | Bantuan langsung dari app |

### 4.3 Di Luar Ruang Lingkup (Non-Goals)
- Admin panel (manajemen produk/pesanan) di mobile — tetap di web Flask/Jinja2 yang sudah ada.
- Dukungan iOS.
- Marketplace multi-seller (aplikasi ini single-seller/single-toko).

---

## 5. User Stories (MVP)

1. *Sebagai pembeli*, saya ingin menjelajahi katalog produk dengan foto & harga jelas, agar mudah memilih produk yang saya inginkan.
2. *Sebagai pembeli*, saya ingin memfilter produk berdasarkan kategori (mis. Minyak Murni), agar pencarian lebih cepat.
3. *Sebagai pembeli*, saya ingin menambahkan produk ke keranjang dan checkout dengan alamat pengiriman, agar proses pemesanan cepat dan jelas.
4. *Sebagai pembeli*, saya ingin membayar langsung di aplikasi (transfer/e-wallet), agar tidak perlu proses manual di luar aplikasi.
5. *Sebagai pembeli*, saya ingin melihat status pesanan saya (Pending → Diproses → Dikirim → Selesai), agar tahu kapan barang akan sampai.
6. *Sebagai pembeli*, saya ingin login agar riwayat pesanan & alamat saya tersimpan untuk pembelian berikutnya.

---

## 6. Metrik Keberhasilan

| Metrik | Target Awal |
|---|---|
| Tingkat keberhasilan checkout (cart → payment sukses) | > 80% |
| Waktu loading katalog produk | < 2 detik (dengan cache) |
| Crash-free session rate | > 98% |
| Tingkat kegagalan pembayaran (payment gateway error) | < 5% |

---

## 7. Risiko & Constraint

| Risiko | Mitigasi |
|---|---|
| Backend Flask perlu direfactor jadi REST API — berpotensi mempengaruhi web yang sudah jalan | Tambahkan endpoint JSON secara terpisah (mis. prefix `/api/v1/...`), jangan ubah route HTML yang sudah ada |
| Harga/stok bisa tidak konsisten antara app & sumber data (kalau divalidasi hanya di client) | Validasi harga & stok akhir selalu di server saat checkout, bukan dipercaya dari input app (detail di `Rules.md`) |
| Integrasi payment gateway pihak ketiga (Midtrans) — kegagalan callback/webhook | Implementasi status "Pending Payment" + reconciliation job untuk cek status transaksi berkala |
| SQLite (dev) tidak cocok untuk produksi dengan banyak transaksi bersamaan | Migrasi ke PostgreSQL untuk tahap produksi (sudah direncanakan di dokumen teknis awal) |
| Keamanan token API/pembayaran di sisi mobile | Gunakan HTTPS wajib, simpan token secara aman (EncryptedSharedPreferences/DataStore), tidak pernah simpan data kartu di device |

---

## 8. Dependensi Dokumen Terkait
- `Architecture.md` — desain teknis sistem, termasuk perubahan backend Flask → REST API
- `Design.md` — desain UI/UX & flow interaksi
- `Schema.md` — struktur data (Room lokal & database backend)
- `Rules.md` — aturan bisnis, keamanan transaksi, dan validasi
