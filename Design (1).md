# Design — Katalog-Minyak (Mobile)
**Dokumen Desain UI/UX**
Versi: 0.1 (Draft Awal) | Tanggal: 28 Agustus 2026

---

## 1. Prinsip Desain

1. **Trust through Clarity** — harga, stok, dan status pesanan harus selalu terlihat jelas; tidak ada ambiguitas di titik-titik transaksi (checkout, pembayaran).
2. **Belanja Cepat** — dari buka app → temukan produk → checkout idealnya minim gesekan (few taps).
3. **Natural & Herbal Mood** — nuansa visual mencerminkan produk (rempah, minyak alami) — warna hangat, earthy, bukan tema e-commerce generik yang dingin.
4. **Transparansi Transaksi** — setiap tahap pembayaran & status pesanan dikomunikasikan jelas, termasuk saat gagal.

---

## 2. Struktur Navigasi Utama

Bottom Navigation Bar 4 tab utama:

```
┌─────────┬─────────┬──────────────┬─────────┐
│ Katalog │ Keranjang│ Pesanan Saya │ Akun    │
└─────────┴─────────┴──────────────┴─────────┘
```

```
┌──────────────┐      ┌───────────────┐      ┌──────────────────┐
│ KatalogScreen│──tap→│ DetailProduk  │──add→│  CartScreen        │
└──────────────┘      └───────────────┘      └─────────┬─────────┘
                                                          │ checkout
                                                          ▼
                                              ┌───────────────────┐
                                              │  CheckoutScreen    │
                                              │ (alamat + ongkir)  │
                                              └─────────┬─────────┘
                                                          │ bayar
                                                          ▼
                                              ┌───────────────────┐
                                              │ Midtrans Payment   │
                                              │ (Snap UI)          │
                                              └─────────┬─────────┘
                                                          ▼
                                              ┌───────────────────┐
                                              │ Order Confirmation │
                                              └───────────────────┘

┌───────────────┐      ┌──────────────────┐
│ Pesanan Saya   │──tap→│ Detail Pesanan    │
│ (History+Status)│      │ (status & item)   │
└───────────────┘      └──────────────────┘

┌──────────┐      ┌───────────────┐
│  Akun    │──────│ Login/Register│ (jika belum login)
└──────────┘      └───────────────┘
   │
   ├── Profil & Alamat
   └── Logout
```

---

## 3. Layar Utama — KatalogScreen

**Elemen:**
- Search bar di atas (pencarian nama produk).
- Chip filter kategori horizontal (Minyak Murni, Rempah Kering, Ekstrak Herbal, dst.), scrollable.
- Grid produk (2 kolom) — tiap card: foto produk, nama, harga, badge kecil jika stok menipis/habis.
- Pull-to-refresh untuk update katalog terbaru.
- Skeleton loading saat fetch pertama kali; tampilan cache langsung terlihat jika sudah pernah dibuka sebelumnya (cache-then-network).

**State:**
- `Loading` (skeleton) → `Loaded` (grid produk) / `Empty` (hasil filter kosong, dengan ilustrasi + saran ubah filter) / `Error` (gagal fetch, tombol coba lagi — namun tetap tampilkan cache lama jika ada).

---

## 4. DetailProdukScreen

**Elemen:**
- Foto produk (bisa carousel jika multi-foto).
- Nama, harga, varian ukuran (dropdown/chip pilihan jika ada beberapa ukuran).
- Deskripsi: manfaat, komposisi, cara penggunaan (expandable/collapsible agar tidak penuh).
- Status stok jelas: "Tersedia", "Stok terbatas (sisa X)", atau "Stok habis" (tombol beli nonaktif jika habis).
- Stepper jumlah (+/-) sebelum tombol "Tambah ke Keranjang".
- Tombol "Tambah ke Keranjang" (primary) tetap terlihat (sticky di bawah layar saat scroll).

---

## 5. CartScreen (Keranjang Belanja)

**Elemen:**
- List item keranjang: foto kecil, nama, varian, harga satuan, stepper jumlah, tombol hapus (swipe atau ikon).
- Ringkasan subtotal otomatis update saat jumlah diubah.
- Tombol "Checkout" besar di bawah, menampilkan total sementara (belum termasuk ongkir).
- Jika keranjang kosong: ilustrasi + tombol "Mulai Belanja" kembali ke Katalog.

---

## 6. CheckoutScreen

**Elemen:**
- Form alamat pengiriman (nama penerima, alamat lengkap, nomor kontak) — bisa pilih dari alamat tersimpan jika sudah pernah diisi.
- Ringkasan item yang dibeli (read-only, collapsible).
- Estimasi ongkos kirim (dihitung ulang otomatis saat alamat berubah).
- Rincian total: subtotal + ongkir = **Total Bayar** (ditampilkan besar & jelas).
- Tombol "Lanjut ke Pembayaran" (primary) → membuka Midtrans Snap UI.

**Prinsip kritis:** Total harga yang ditampilkan di layar ini adalah hasil dari server (bukan dihitung ulang secara lokal di app), agar konsisten dengan yang benar-benar akan ditagihkan.

---

## 7. Alur Pembayaran (Midtrans)

```
[CheckoutScreen: tap "Lanjut ke Pembayaran"]
      │
      ▼
[Loading singkat: "Menyiapkan pembayaran..."]
      │
      ▼
[Midtrans Snap UI terbuka — pilih metode: transfer/e-wallet/dll]
      │
      ├─ Sukses ──► [Order Confirmation Screen: ✅ "Pesanan Berhasil Dibuat"]
      │
      ├─ Pending (mis. menunggu transfer) ──► [Order Confirmation: ⏳ "Menunggu Pembayaran" + instruksi]
      │
      └─ Gagal/Dibatalkan ──► [Kembali ke Checkout, tampilkan pesan jelas + tombol "Coba Lagi"]
```

---

## 8. Pesanan Saya (Order History) & Detail Pesanan

**List:**
- Dikelompokkan/diurutkan dari terbaru, tiap item: thumbnail produk pertama, jumlah item, total harga, badge status berwarna (Pending = kuning, Diproses = biru, Dikirim = ungu, Selesai = hijau).

**Detail Pesanan:**
- Timeline status visual (Pending → Diproses → Dikirim → Selesai), menyoroti status saat ini.
- Rincian item, alamat pengiriman, total pembayaran, metode pembayaran yang dipakai.
- Tombol "Refresh Status" manual (untuk MVP, sebelum ada push notification).

---

## 9. AkunScreen

- Jika belum login: tombol Login/Register menonjol, tetap bisa browsing katalog tanpa login (guest browsing), tapi checkout wajib login.
- Jika sudah login: foto/avatar placeholder, nama, email, menu: Profil & Alamat Tersimpan, Riwayat Pesanan (shortcut), Logout.

---

## 10. Visual & Interaction Language

| Elemen | Pendekatan |
|---|---|
| Warna Primer | Palet earthy/hangat (mis. hijau zaitun, cokelat kayu manis, krem) mencerminkan produk herbal/alami |
| Tipografi | Material 3 Typography, hierarki jelas antara nama produk (medium-bold) vs harga (bold, warna aksen) vs deskripsi (regular) |
| Foto Produk | Rasio konsisten (mis. 1:1), rounded corner ringan, konsisten di seluruh grid & detail |
| Badge Status Pesanan | Warna berbeda per status agar sekali lihat langsung paham (bukan hanya teks) |
| Dark/Light Mode | Mendukung keduanya |
| Bahasa | Bahasa Indonesia sebagai default (sesuai target pasar) |

---

## 11. Error & Edge Case Handling (UX)

| Kondisi | Perilaku UI |
|---|---|
| Stok berubah jadi habis saat checkout (race condition) | Server tolak order, app tampilkan pesan jelas per item yang bermasalah, kembalikan ke Cart untuk disesuaikan |
| Ongkir gagal dihitung (API kurir down) | Fallback: minta user pilih ulang/coba lagi, jangan biarkan checkout lanjut dengan ongkir kosong/salah |
| Pembayaran gagal/timeout | Order tetap tersimpan berstatus `pending_payment`/`failed`, muncul di Pesanan Saya dengan opsi "Bayar Lagi" |
| Tidak ada koneksi internet saat checkout/pembayaran | Tombol checkout/bayar dinonaktifkan dengan pesan jelas; katalog tetap bisa dilihat dari cache |
| Sesi login kadaluarsa (token expired) di tengah checkout | Arahkan ke login ulang, keranjang tetap tersimpan lokal agar tidak hilang |
