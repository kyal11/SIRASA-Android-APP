# 📱 Sirasa App (Android)

**Sirasa App** adalah aplikasi mobile Android untuk sistem peminjaman ruang diskusi di perpustakaan **UPN Veteran Jakarta**. Aplikasi ini terhubung dengan backend [sirasa-service](https://github.com/kyal11/SIRASA-Service) dan dirancang untuk memudahkan mahasiswa dalam melakukan booking ruang diskusi, validasi QR Code, dan mendapatkan notifikasi terkait peminjaman.

---

## 🚀 Fitur Utama

- 📅 Booking ruangan diskusi berdasarkan waktu & kapasitas
- 🔄 Sistem rekomendasi ruangan berbasis **algoritma Greedy**
- 🔐 Login & Registrasi menggunakan autentikasi JWT
- 📲 Scan & generate QR Code untuk validasi peminjaman
- 🔔 Notifikasi push melalui Firebase Cloud Messaging (FCM)
- 📦 Manajemen profil pengguna
---

## 🛠️ Teknologi yang Digunakan

- **Kotlin + Jetpack Compose** – Modern Android UI Toolkit
- **Hilt** – Dependency Injection
- **Retrofit + Gson** – Networking API
- **Socket.IO** – Komunikasi real-time (admin)
- **Paging 3** – Paginasi data booking
- **CameraX + ML Kit** – Pemindaian QR Code
- **Firebase Messaging** – Notifikasi push
- **Jetpack DataStore** – Penyimpanan data lokal
- **Lottie** – Animasi interaktif
---

## ⚙️ Cara Install & Menjalankan

### 1. Clone Repository

```bash
git clone https://github.com/namamu/sirasa-app.git
cd sirasa-app
```
### 2. Buat dan isi gradle.properties
```bash
API_BASE_URL=http://10.0.2.2:3000
Gantilah dengan URL backend sesuai lingkungan lokal atau production Anda.
```
### 3. Buka di Android Studio
```bash
Gunakan Android Studio Hedgehog (2023.1.1+) atau yang lebih terbaru

Sinkronisasi Gradle

Jalankan di emulator atau perangkat Android minimal API 24 (Android 7.0)
```

##  🧪 Testing
✅ Unit Test dengan JUnit 5
