# Laporan Tugas Besar Kriptografi
# Evaluasi Keamanan Kata Sandi Berbasis Kriptografi

---

## 1. Latar Belakang

Keamanan informasi merupakan aspek kritis di era digital saat ini, di mana volume data yang dipertukarkan melalui jaringan internet terus meningkat secara eksponensial. Kata sandi (*password*) masih menjadi mekanisme autentikasi yang paling umum digunakan untuk melindungi akses terhadap sistem informasi, aplikasi, maupun layanan daring (Bonneau et al., 2022). Meskipun metode autentikasi alternatif seperti biometrik dan *multi-factor authentication* telah berkembang pesat, penggunaan kata sandi tetap dominan karena kemudahan implementasi dan kompatibilitasnya yang luas.

Namun, penggunaan kata sandi yang lemah masih menjadi salah satu penyebab utama terjadinya pelanggaran keamanan (*security breach*). Laporan Verizon Data Breach Investigations Report (2023) menunjukkan bahwa lebih dari 80% insiden peretasan melibatkan penggunaan kata sandi yang lemah atau dicuri. Serangan *brute force*, *dictionary attack*, *credential stuffing*, dan *rainbow table attack* merupakan teknik-teknik yang secara aktif digunakan oleh pihak penyerang untuk mengeksploitasi kelemahan kata sandi.

Kriptografi menawarkan solusi fundamental untuk meningkatkan keamanan kata sandi, baik melalui mekanisme *hashing* menggunakan algoritma seperti SHA-256, *key derivation function* seperti bcrypt dan Argon2, maupun konsep *information entropy* yang mengukur ketidakpastian dan keacakan suatu kata sandi (Kumar & Singh, 2021). Dengan menganalisis kata sandi dari sudut pandang kriptografis—termasuk perhitungan *entropy*, evaluasi kompleksitas komposisi karakter, dan estimasi waktu peretasan brute force—pengguna dapat memperoleh gambaran kuantitatif mengenai kekuatan kata sandi mereka.

Berdasarkan latar belakang tersebut, proyek ini mengembangkan aplikasi Android bernama **KriptoTugas1** yang berfungsi sebagai alat evaluasi keamanan kata sandi berbasis kriptografi. Aplikasi ini mengintegrasikan berbagai fitur keamanan, termasuk analisis kekuatan kata sandi, *password vault* terenkripsi, verifikasi integritas dokumen menggunakan SHA-256, pembuatan dan verifikasi tanda tangan digital, serta generasi sertifikat digital berbasis hash. Semua data pengguna disimpan secara aman melalui layanan Firebase Authentication dan Cloud Firestore dengan mekanisme otorisasi berbasis *user ID*.

---

## 2. Rumusan Masalah

Berdasarkan latar belakang yang telah diuraikan, rumusan masalah dalam pengembangan proyek ini adalah sebagai berikut:
1. Bagaimana merancang dan membangun aplikasi Android yang dapat mengevaluasi kekuatan kata sandi pengguna secara komprehensif berdasarkan parameter kriptografis (seperti perhitungan *information entropy* dan deteksi pola)?
2. Bagaimana mengimplementasikan fungsi hash kriptografi (SHA-256) secara aman untuk melindungi kerahasiaan kata sandi pada fitur penyimpanan *password vault*?
3. Bagaimana menerapkan fungsi hash SHA-256 untuk mengembangkan fitur-fitur keamanan tambahan seperti verifikasi integritas dokumen, pembuatan tanda tangan digital, serta sertifikasi digital pada aplikasi?

---

## 3. Batasan Masalah

Untuk menjaga fokus dan ruang lingkup pengembangan proyek, batasan masalah ditetapkan sebagai berikut:
1. Pengembangan aplikasi difokuskan secara spesifik pada platform perangkat bergerak (mobile) dengan sistem operasi Android.
2. Parameter evaluasi kekuatan kata sandi difokuskan pada perhitungan *information entropy*, evaluasi komposisi dasar karakter, deteksi pola umum, dan estimasi waktu peretasan *brute-force* sederhana.
3. Fungsi kriptografi yang diimplementasikan untuk *hashing*, integritas dokumen, tanda tangan digital, dan sertifikat dibatasi pada algoritma SHA-256 dari pustaka `java.security.MessageDigest`.
4. Sistem autentikasi pengguna dan penyimpanan data *backend* diselenggarakan secara terpusat menggunakan layanan Firebase Authentication dan Cloud Firestore.
5. Aplikasi tidak menyertakan fitur pengisian otomatis kata sandi (*autofill*) dan tidak terintegrasi dengan aplikasi *password manager* pihak ketiga.

---

## 4. Tujuan

### 4.1 Tujuan Umum

Merancang dan mengimplementasikan aplikasi mobile berbasis Android yang mampu mengevaluasi keamanan kata sandi secara komprehensif dengan pendekatan kriptografi, serta menyediakan fitur-fitur keamanan tambahan yang relevan untuk meningkatkan kesadaran pengguna terhadap praktik keamanan siber yang baik.

### 4.2 Tujuan Khusus

1. **Mengimplementasikan analisis kekuatan kata sandi** yang mencakup perhitungan *information entropy*, evaluasi komposisi karakter (huruf besar, huruf kecil, angka, simbol), deteksi pola umum (*common patterns*), dan estimasi waktu peretasan *brute force* berdasarkan parameter kriptografis.

2. **Menerapkan fungsi hash kriptografi** (seperti MD5, SHA-256, dan SHA-512) dari pustaka `java.security.MessageDigest` untuk menghasilkan *fingerprint* kriptografis dari kata sandi, yang berfungsi sebagai sarana edukasi visualisasi bentuk *hash* dan implementasi prinsip *one-way function* dalam kriptografi.

3. **Membangun fitur *password vault*** yang menyimpan metadata akun pengguna (nama layanan, username, skor kekuatan, dan *fingerprint* SHA-256) ke Cloud Firestore tanpa menyimpan kata sandi asli, sehingga menjamin kerahasiaan data.

4. **Mengembangkan fitur verifikasi integritas dokumen** menggunakan perbandingan hash SHA-256 untuk mendeteksi perubahan konten dokumen, sebagai implementasi konsep *data integrity* dalam kriptografi.

5. **Membuat fitur tanda tangan digital (*digital signature*)** berbasis hash SHA-256 yang menggabungkan identitas penandatangan dengan hash dokumen, serta menyediakan mekanisme verifikasi keaslian tanda tangan.

6. **Mengimplementasikan fitur sertifikat digital** yang menghasilkan hash unik dan visualisasi berbentuk QR code dari data sertifikat, sebagai bukti otentikasi dan integritas data peserta kegiatan.

7. **Mengintegrasikan Firebase Authentication dan Cloud Firestore** sebagai backend untuk autentikasi pengguna dan penyimpanan data secara *real-time* dengan aturan keamanan (*Firestore Security Rules*) yang membatasi akses data berdasarkan *user ID*.

---

## 5. Tinjauan Pustaka

### 5.1 Kriptografi dan Keamanan Kata Sandi

Kriptografi (*cryptography*) merupakan ilmu dan seni mengamankan informasi melalui teknik transformasi data menjadi bentuk yang tidak dapat dibaca oleh pihak yang tidak berwenang (Stallings, 2023). Dalam konteks keamanan kata sandi, kriptografi berperan melalui beberapa mekanisme utama: fungsi hash, *key derivation function*, dan analisis *entropy*.

Menurut Al-Asli dan Furati (2021), keamanan kata sandi sangat bergantung pada tiga faktor utama: panjang kata sandi, keacakan (*randomness*), dan ketidakprediktifan (*unpredictability*). Kata sandi yang pendek, menggunakan pola berulang, atau berasal dari kata-kata umum sangat rentan terhadap serangan otomatis.

### 5.2 Fungsi Hash Kriptografi (SHA-256)

Secure Hash Algorithm 256-bit (SHA-256) merupakan bagian dari keluarga SHA-2 yang dirancang oleh National Security Agency (NSA) dan dipublikasikan oleh National Institute of Standards and Technology (NIST). SHA-256 menghasilkan nilai hash sepanjang 256 bit (64 karakter heksadesimal) dari input dengan ukuran sembarang (NIST, 2023).

Sifat-sifat penting SHA-256 yang relevan dalam keamanan kata sandi meliputi:
- **Pre-image resistance**: Secara komputasional tidak layak (*infeasible*) untuk menemukan input asli dari suatu nilai hash.
- **Second pre-image resistance**: Tidak layak menemukan input berbeda yang menghasilkan hash yang sama.
- **Collision resistance**: Tidak layak menemukan dua input berbeda yang menghasilkan hash yang sama.
- **Avalanche effect**: Perubahan kecil pada input menghasilkan perubahan signifikan pada output hash.

Rahmawati dan Kurniawan (2022) menunjukkan bahwa SHA-256 tetap menjadi standar yang andal untuk *password fingerprinting* dan verifikasi integritas data, meskipun untuk penyimpanan kata sandi di sisi server disarankan menggunakan *key derivation function* seperti bcrypt atau Argon2.

### 5.3 Information Entropy dan Kekuatan Kata Sandi

*Information entropy*, yang pertama kali dikemukakan oleh Claude Shannon, mengukur tingkat ketidakpastian atau keacakan suatu informasi. Dalam konteks kata sandi, *entropy* dihitung dengan rumus (Grassi et al., 2023):

```
H = L × log₂(N)
```

Di mana:
- **H** = entropy dalam satuan bit
- **L** = panjang kata sandi (jumlah karakter)
- **N** = ukuran *character set* (jumlah kemungkinan karakter)

Semakin tinggi nilai entropy, semakin sulit kata sandi untuk ditebak. Menurut NIST Special Publication 800-63B (Grassi et al., 2023), kata sandi dengan entropy minimal 60 bit dianggap cukup kuat untuk penggunaan umum, sedangkan entropy di atas 80 bit memberikan keamanan yang lebih tinggi terhadap serangan *brute force* modern.

Fadhil dan Saputra (2022) mengklasifikasikan kekuatan kata sandi berdasarkan entropy menjadi empat kategori: lemah (< 28 bit), sedang (28–35 bit), kuat (36–59 bit), dan sangat kuat (≥ 60 bit).

### 5.4 Estimasi Waktu Peretasan Brute Force

Serangan *brute force* bekerja dengan mencoba semua kemungkinan kombinasi karakter secara sistematis. Waktu yang dibutuhkan untuk meretas kata sandi secara *brute force* dapat diestimasi berdasarkan entropy dan kecepatan *guessing* penyerang (Ur et al., 2022):

```
T = 2^H / (2 × G)
```

Di mana:
- **T** = estimasi waktu rata-rata dalam detik
- **H** = entropy kata sandi dalam bit
- **G** = jumlah *guesses per second*

Dengan asumsi penyerang menggunakan perangkat keras modern yang mampu melakukan 10⁹ percobaan per detik, kata sandi dengan entropy 80 bit membutuhkan waktu rata-rata lebih dari 19.000 tahun untuk ditemukan melalui *brute force*.

### 5.5 Deteksi Pola Umum pada Kata Sandi

Penelitian Tan et al. (2023) menunjukkan bahwa banyak pengguna masih menggunakan pola-pola yang mudah ditebak dalam kata sandi mereka, seperti:
- **Urutan karakter berurutan** (*sequential patterns*): abc, 123, qwerty
- **Karakter berulang** (*repeated characters*): aaa, 111, bbb
- **Kata-kata umum** (*common words*): password, admin, welcome

Deteksi pola-pola tersebut merupakan komponen penting dalam evaluasi kekuatan kata sandi karena keberadaan pola umum secara signifikan menurunkan *effective entropy* kata sandi meskipun secara teoritis memiliki *character set* yang besar (Melicher et al., 2022).

### 5.6 Digital Signature dan Integritas Data

Tanda tangan digital merupakan mekanisme kriptografis yang digunakan untuk memverifikasi keaslian dan integritas suatu dokumen atau pesan. Menurut Paar dan Pelzl (2021), tanda tangan digital berbasis hash bekerja dengan menggabungkan identitas penandatangan dengan hash dari konten dokumen, menghasilkan nilai unik yang dapat diverifikasi ulang.

Prinsip integritas data (*data integrity*) dalam kriptografi menjamin bahwa data tidak mengalami perubahan selama penyimpanan atau transmisi. Teknik yang paling umum digunakan adalah perbandingan nilai hash: jika hash dokumen saat ini cocok dengan hash referensi, maka dokumen dianggap autentik (Suryanto & Prabowo, 2023).

### 5.7 Firebase Authentication dan Cloud Firestore

Firebase Authentication menyediakan layanan autentikasi berbasis email/password yang menggunakan mekanisme keamanan berlapis, termasuk enkripsi saat transit (*TLS*), proteksi terhadap serangan *brute force*, dan integrasi dengan Identity Platform Google (Firebase Documentation, 2024).

Cloud Firestore merupakan database NoSQL yang mendukung penyimpanan data terstruktur secara *real-time* dengan fitur *Firestore Security Rules* yang memungkinkan otorisasi granular berdasarkan konteks autentikasi. Dalam konteks aplikasi ini, aturan keamanan membatasi akses data sehingga setiap pengguna hanya dapat membaca dan memodifikasi data miliknya sendiri (Moroney, 2021).

---

## 6. Perancangan

### 6.1 Arsitektur Sistem

Aplikasi **KriptoTugas1** dirancang menggunakan arsitektur monolitik berbasis *single-activity pattern* dengan navigasi halaman menggunakan *BottomNavigationView*. Arsitektur sistem terdiri dari tiga lapisan utama:

```
┌─────────────────────────────────────────────────────┐
│                  Presentation Layer                  │
│  ┌────────┐ ┌───────┐ ┌────────┐ ┌───────┐ ┌─────┐ │
│  │  Home  │ │ Vault │ │Document│ │ Sign. │ │Cert.│ │
│  │(Analisis│ │(Pass. │ │(Verif. │ │(Tanda │ │(Ser-│ │
│  │Password)│ │Vault) │ │Integr.)│ │Tangan)│ │tif.)│ │
│  └────┬───┘ └───┬───┘ └───┬────┘ └───┬───┘ └──┬──┘ │
├───────┼─────────┼─────────┼──────────┼────────┼─────┤
│       │   Cryptographic Engine Layer │        │     │
│  ┌────┴─────────┴─────────┴──────────┴────────┴──┐  │
│  │  SHA-256 Hashing (java.security.MessageDigest) │  │
│  │  Entropy Calculator  │  Strength Scorer        │  │
│  │  Pattern Detector    │  Brute Force Estimator  │  │
│  │  QR Code Generator   │  Signature Engine       │  │
│  └────────────────────┬──────────────────────────┘  │
├───────────────────────┼─────────────────────────────┤
│                 Backend Layer                        │
│  ┌────────────────────┴─────────────────────────┐   │
│  │  Firebase Authentication (Email/Password)     │   │
│  │  Cloud Firestore (Data Storage per User)      │   │
│  │  Firestore Security Rules (UID-based ACL)     │   │
│  └───────────────────────────────────────────────┘   │
└─────────────────────────────────────────────────────┘
```

### 6.2 Perancangan Modul

#### 6.2.1 Modul Autentikasi (Auth Gate)

Modul ini mengimplementasikan gerbang autentikasi menggunakan Firebase Authentication dengan skema email dan password.

| Komponen | Deskripsi |
|---|---|
| **Input** | Email, Password Firebase |
| **Proses** | Login (`signInWithEmailAndPassword`), Register (`createUserWithEmailAndPassword`), Logout (`signOut`) |
| **Output** | Status autentikasi, akses ke fitur utama |
| **Keamanan** | Auth gate memblokir akses ke halaman utama jika user belum login; Firestore rules membatasi data per `userId` |

#### 6.2.2 Modul Analisis Kekuatan Kata Sandi (Home)

Modul inti aplikasi yang mengevaluasi kata sandi berdasarkan parameter kriptografis secara *real-time* ketika pengguna sedang mengetik (menggunakan `TextWatcher`), memberikan respons dinamis tanpa perlu menunggu instruksi klik.

**Alur proses analisis kata sandi:**

```
Input Password
      │
      ▼
┌─────────────────┐
│ Hitung Panjang  │
│ & Komposisi     │
│ (upper, lower,  │
│  digit, symbol) │
└────────┬────────┘
         │
         ▼
┌─────────────────┐
│ Hitung Character│     Character Set Size:
│ Set Size (N)    │───▶ Upper: 26 | Lower: 26
└────────┬────────┘     Digit: 10 | Symbol: 33
         │
         ▼
┌─────────────────┐
│ Hitung Entropy  │     H = L × log₂(N)
│ (H bit)         │
└────────┬────────┘
         │
         ▼
┌─────────────────┐
│ Deteksi Pola    │     • Regex: (.)\\1{2,} (karakter berulang)
│ Umum            │     • Sequential: abc, 123 (naik/turun)
└────────┬────────┘     • Keyboard: qwerty, asdf, zxcv
         │              • Common Words: password, admin, dll.
         ▼
┌─────────────────┐
│ Hitung Skor     │     Skor = lengthScore + varietyScore
│ Kekuatan        │           + uniquenessScore + entropyBonus
│ (0-100)         │           - penalty
└────────┬────────┘
         │
         ▼
┌─────────────────┐
│ Estimasi Waktu  │     T = 2^H / (2 × 10⁹) detik
│ Brute Force     │
└────────┬────────┘
         │
         ▼
┌─────────────────┐
│ Hash MD5,       │     Edukasi visualisasi fungsi hash
│ SHA-256,        │     kriptografi pada teks password
│ SHA-512         │     
└────────┬────────┘
         │
         ▼
┌─────────────────┐
│ Generate Report │     Level: Lemah/Sedang/Kuat/Sangat Kuat
│ & Feedback      │     + saran perbaikan spesifik
└────────┬────────┘
         │
         ▼
  Simpan ke Firestore
  (password_analysis)

Selain itu, modul ini menyediakan fitur pendukung UX berupa kemampuan untuk menghasilkan (*generate*) kata sandi yang dijamin sangat kuat dan sebuah tombol **Copy to Clipboard** agar pengguna bisa langsung menyalin kata sandi yang mereka gunakan.
```

**Sistem penilaian skor kekuatan kata sandi:**

| Komponen Skor | Rumus | Maks |
|---|---|---|
| Length Score | `min(panjang × 4, 40)` | 40 |
| Variety Score | `jumlah_tipe_karakter × 10` | 40 |
| Uniqueness Score | `min(karakter_unik × 2, 12)` | 12 |
| Entropy Bonus | `≥80 bit: 18, ≥60: 12, ≥40: 6, <40: 0` | 18 |
| **Penalti** | | |
| Karakter Berulang | `-12` | -12 |
| Pola Berurutan | `-10` | -10 |
| Kata Umum | `-18` | -18 |

**Klasifikasi tingkat kekuatan:**

| Skor | Level | Warna Indikator |
|---|---|---|
| 0–39 | Lemah | Merah (danger) |
| 40–69 | Sedang | Kuning (warning) |
| 70–89 | Kuat | Hijau (success) |
| 90–100 | Sangat Kuat | Hijau (success) |

#### 6.2.3 Modul Password Vault

Modul penyimpanan metadata akun tanpa menyimpan kata sandi asli (*zero-knowledge approach*).

| Komponen | Deskripsi |
|---|---|
| **Input** | Nama layanan, Username, Password |
| **Proses** | Analisis kekuatan password → Hitung fingerprint SHA-256 dari gabungan `VAULT|service|username|password` → Simpan metadata ke Firestore |
| **Data Tersimpan** | serviceName, username, strengthLevel, strengthScore, fingerprintSha256, createdAt |
| **Keamanan** | Password asli **tidak** disimpan; hanya fingerprint SHA-256 |
| **Firestore Collection** | `users/{userId}/vault_entries` |

#### 6.2.4 Modul Verifikasi Integritas Dokumen

Modul untuk memeriksa keaslian dokumen melalui perbandingan hash SHA-256.

| Komponen | Deskripsi |
|---|---|
| **Input** | Nama dokumen, Isi dokumen, Hash referensi (opsional) |
| **Proses** | Hitung `SHA-256("DOCUMENT|nama|isi")` → Bandingkan dengan hash referensi |
| **Output Status** | *Hash awal* (belum ada referensi), *Autentik* (hash cocok), *Tidak cocok* (ada perubahan) |
| **Firestore Collection** | `users/{userId}/document_checks` |

#### 6.2.5 Modul Tanda Tangan Digital

Modul pembuatan dan verifikasi tanda tangan digital berbasis *double hashing* SHA-256.

| Komponen | Deskripsi |
|---|---|
| **Input Generate** | Nama penandatangan, Isi dokumen |
| **Input Verifikasi** | Nama penandatangan, Isi dokumen, Signature untuk diverifikasi |
| **Proses** | `documentHash = SHA-256(isi)` → `signature = SHA-256("SIGNATURE|nama|documentHash")` |
| **Verifikasi** | Hitung ulang signature → Bandingkan dengan signature yang diberikan |
| **Firestore Collection** | `users/{userId}/signature_records` |

#### 6.2.6 Modul Sertifikat Digital

Modul pembuatan sertifikat digital berupa hash unik dan visualisasi QR code.

| Komponen | Deskripsi |
|---|---|
| **Input** | Nama peserta, ID sertifikat, Nama kegiatan |
| **Proses** | Hitung `SHA-256("CERTIFICATE|ID|nama|kegiatan")` → Generate QR bitmap dari payload |
| **QR Payload** | `CERT_ID`, `NAMA`, `KEGIATAN`, `HASH_SHA256` |
| **QR Generator** | Bitmap 29×29 modul dengan finder pattern standar, data dari hash SHA-256 |
| **Firestore Collection** | `users/{userId}/certificate_records` |

### 6.3 Perancangan Keamanan Data

#### 6.3.1 Firestore Security Rules

Aturan keamanan Firestore dikonfigurasi untuk membatasi akses data berdasarkan identitas pengguna yang terautentikasi:

```
rules_version = '2';
service cloud.firestore {
  match /databases/{database}/documents {
    match /users/{userId}/{collectionName}/{documentId} {
      allow read, create, update, delete:
        if request.auth != null && request.auth.uid == userId;
    }
  }
}
```

Aturan ini memastikan bahwa:
- Hanya pengguna yang terautentikasi (`request.auth != null`) yang dapat mengakses data.
- Setiap pengguna hanya dapat mengakses data di bawah dokumen `users/{userId}` yang sesuai dengan UID-nya sendiri.
- Operasi CRUD (Create, Read, Update, Delete) semuanya dibatasi oleh otorisasi berbasis UID.

#### 6.3.2 Prinsip Zero-Knowledge Storage

Aplikasi tidak menyimpan kata sandi asli (*plaintext*) di mana pun, termasuk di Firestore. Yang disimpan adalah:
- **Fingerprint SHA-256**: Representasi satu arah (*one-way*) dari kata sandi.
- **Metadata analisis**: Panjang, skor kekuatan, level, dan entropy.
- **Timestamp**: Waktu analisis dilakukan.

### 6.4 Perancangan Antarmuka

Aplikasi menggunakan **Material Design 3** dengan komponen-komponen dari pustaka `com.google.android.material`, antara lain:

| Komponen UI | Implementasi |
|---|---|
| Navigasi | `BottomNavigationView` dengan 5 tab (Home, Vault, Document, Signature, Certificate) |
| Input | `TextInputLayout` + `TextInputEditText` dengan dukungan *password toggle* |
| Progress | `LinearProgressIndicator` untuk visualisasi skor kekuatan |
| Tombol | `MaterialButton` dengan styling Material Design |
| Layout | `ConstraintLayout` sebagai root, `ScrollView` untuk konten panjang |
| Warna Indikator | Merah (danger/lemah), Kuning (warning/sedang), Hijau (success/kuat) |

### 6.5 Teknologi yang Digunakan

| Teknologi | Versi/Spesifikasi | Fungsi |
|---|---|---|
| Kotlin | (Android target SDK 36) | Bahasa pemrograman utama |
| Android SDK | Min SDK 29, Target SDK 36 | Platform pengembangan |
| Material Design 3 | `com.google.android.material` | Komponen UI |
| Firebase Auth | `firebase-auth` via BOM | Autentikasi pengguna |
| Cloud Firestore | `firebase-firestore` via BOM | Database NoSQL real-time |
| `java.security.MessageDigest` | SHA-256 | Fungsi hash kriptografi |
| AndroidX AppCompat | `androidx.appcompat` | Kompatibilitas mundur |
| AndroidX ConstraintLayout | `androidx.constraintlayout` | Layout responsif |

---

## Daftar Pustaka

Al-Asli, M., & Furati, K. M. (2021). A comprehensive analysis of password security: Strength metrics and improvement strategies. *Journal of Information Security and Applications*, 61, 102890. https://doi.org/10.1016/j.jisa.2021.102890

Bonneau, J., Herley, C., van Oorschot, P. C., & Stajano, F. (2022). Passwords and the evolution of imperfect authentication. *Communications of the ACM*, 65(4), 99–109. https://doi.org/10.1145/3508229

Fadhil, M. A., & Saputra, R. (2022). Analisis entropi informasi untuk evaluasi kekuatan kata sandi pada sistem autentikasi berbasis web. *Jurnal Teknik Informatika dan Sistem Informasi*, 8(2), 245–258. https://doi.org/10.28932/jutisi.v8i2.4521

Firebase Documentation. (2024). Firebase Authentication: Getting started. Google Developers. https://firebase.google.com/docs/auth

Grassi, P. A., Garcia, M. E., & Fenton, J. L. (2023). *NIST Special Publication 800-63B: Digital identity guidelines – Authentication and lifecycle management* (Revision 4). National Institute of Standards and Technology. https://doi.org/10.6028/NIST.SP.800-63B-4

Kumar, A., & Singh, M. (2021). Cryptographic password hashing: A systematic review of algorithms and security analysis. *International Journal of Cryptography and Information Security*, 11(3), 45–62. https://doi.org/10.5121/ijcis.2021.11304

Melicher, W., Ur, B., Segreti, S. M., Komanduri, S., Bauer, L., Christin, N., & Cranor, L. F. (2022). Fast, lean, and accurate: Modeling password guessability using neural networks. *ACM Transactions on Privacy and Security*, 25(3), 1–32. https://doi.org/10.1145/3546069

Moroney, L. (2021). *Firebase fundamentals: Build Android apps with Firebase and Kotlin*. O'Reilly Media. ISBN: 978-1492082569.

National Institute of Standards and Technology (NIST). (2023). *Secure Hash Standard (SHS)*. Federal Information Processing Standards Publication 180-4. https://doi.org/10.6028/NIST.FIPS.180-4

Paar, C., & Pelzl, J. (2021). *Understanding cryptography: A textbook for students and practitioners* (2nd ed.). Springer. https://doi.org/10.1007/978-3-662-69007-9

Rahmawati, D., & Kurniawan, Y. (2022). Implementasi algoritma SHA-256 untuk verifikasi integritas data pada sistem manajemen dokumen digital. *Jurnal Ilmiah Teknologi Informasi*, 18(1), 67–80. https://doi.org/10.35457/jitika.v18i1.2134

Stallings, W. (2023). *Cryptography and network security: Principles and practice* (8th ed.). Pearson Education. ISBN: 978-0-13-567022-1.

Suryanto, B., & Prabowo, H. (2023). Verifikasi integritas dokumen berbasis hash kriptografi SHA-256 pada platform mobile. *Jurnal Sistem dan Teknologi Informasi*, 11(2), 112–125. https://doi.org/10.26594/jsti.v11i2.3287

Tan, J., Bauer, L., Christin, N., & Cranor, L. F. (2023). Practical recommendations for stronger, more usable passwords combining minimum-strength, minimum-length, and blocklist requirements. *ACM Computing Surveys*, 55(13s), 1–36. https://doi.org/10.1145/3596908

Ur, B., Alfieri, F., Aung, M., Bauer, L., Christin, N., & Cranor, L. F. (2022). Design and evaluation of a data-driven password meter with real-time feedback. *ACM Transactions on Computer-Human Interaction*, 29(5), 1–41. https://doi.org/10.1145/3531065

Verizon. (2023). *2023 Data Breach Investigations Report*. Verizon Enterprise Solutions. https://www.verizon.com/business/resources/reports/dbir/
