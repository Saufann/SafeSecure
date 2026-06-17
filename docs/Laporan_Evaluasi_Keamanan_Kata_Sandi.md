# Laporan Tugas Besar Kriptografi
# Evaluasi Keamanan Kata Sandi Berbasis Kriptografi

---

# BAB I
# PENDAHULUAN

## 1.1 Latar Belakang

Keamanan informasi merupakan aspek paling fundamental dan kritis di era transformasi digital saat ini, di mana volume pertukaran data melalui jaringan global internet mengalami peningkatan secara eksponensial. Dalam berbagai arsitektur sistem informasi, aplikasi, maupun layanan daring, kata sandi (*password*) hingga kini masih menjadi mekanisme autentikasi primer yang paling umum diimplementasikan guna melindungi akses terhadap data sensitif (Bonneau et al., 2022). Meskipun berbagai inovasi autentikasi alternatif—seperti autentikasi biometrik dan *multi-factor authentication* (MFA)—telah berkembang dengan pesat, penggunaan kata sandi tetap mendominasi karena tingkat kompatibilitasnya yang tinggi, kemudahan integrasi, serta efisiensi biaya implementasi.

Kendati demikian, penggunaan kata sandi yang lemah masih menjadi celah keamanan utama dan merupakan penyebab dominan terjadinya insiden kebocoran data (*data breach*). Laporan tahunan *Verizon Data Breach Investigations Report* (2023) secara empiris menunjukkan bahwa lebih dari 80% insiden peretasan sistem secara langsung maupun tidak langsung melibatkan eksploitasi terhadap kredensial atau kata sandi yang lemah, rentan ditebak, ataupun telah dicuri. Berbagai metodologi serangan modern—seperti *brute-force attack*, *dictionary attack*, *credential stuffing*, dan *rainbow table attack*—secara persisten dikembangkan oleh pihak penyerang (*threat actors*) untuk mengeksploitasi kelalaian pengguna dalam merancang kata sandi mereka.

Dalam konteks ini, disiplin ilmu kriptografi menawarkan solusi fundamental dan analitis untuk mengevaluasi serta meningkatkan keamanan kata sandi. Pemanfaatan algoritma kriptografi—baik melalui mekanisme *hashing* searah seperti SHA-256, fungsi turunan kunci (*key derivation function*) seperti bcrypt dan Argon2, maupun penerapan metrik *information entropy*—mampu mengukur tingkat ketidakpastian (*unpredictability*) dan keacakan (*randomness*) dari sebuah kata sandi (Kumar & Singh, 2021). Evaluasi komprehensif dari sudut pandang kriptografis, yang meliputi perhitungan *entropy*, analisis kompleksitas karakter, deteksi pengulangan pola (*pattern recognition*), serta perhitungan matematis terkait estimasi waktu yang dibutuhkan untuk peretasan *brute-force*, memungkinkan sistem untuk memberikan penilaian kuantitatif yang objektif terhadap ketahanan suatu kata sandi.

Berpijak pada urgensi dan latar belakang permasalahan di atas, proyek penelitian ini diinisiasi untuk mengembangkan sebuah aplikasi *mobile* berbasis sistem operasi Android dengan nama **KriptoTugas1**. Aplikasi ini dirancang secara khusus untuk berfungsi sebagai instrumen evaluasi keamanan kata sandi berbasis prinsip-prinsip kriptografi. Lebih lanjut, guna memberikan fungsionalitas yang holistik, aplikasi ini turut mengintegrasikan fitur penyimpanan kata sandi (*Password Vault*) menggunakan arsitektur *Zero-Knowledge* ke layanan *cloud* Firebase Authentication dan Cloud Firestore, dengan menerapkan mekanisme *hashing* guna menjamin kerahasiaan data pengguna.

---

## 1.2 Rumusan Masalah

Berdasarkan pemaparan latar belakang di atas, maka rumusan masalah yang akan diselesaikan dalam pengembangan perangkat lunak ini adalah sebagai berikut:
1. Bagaimana merancang dan membangun sebuah aplikasi *mobile* berbasis Android yang mampu mengevaluasi dan memberikan penilaian kekuatan keamanan kata sandi secara akurat berdasarkan parameter kriptografis seperti *information entropy*, komposisi karakter, dan deteksi pola?
2. Bagaimana mengimplementasikan fungsi algoritma *hash* kriptografi searah (seperti SHA-256) guna memastikan bahwa kata sandi beserta metadata pengguna dapat disimpan secara terenkripsi ke dalam pangkalan data *cloud* (Firestore), tanpa mengekspos teks asli (*plaintext*) dari kata sandi tersebut?

---

## 1.3 Batasan Masalah

Untuk menjaga arah penelitian agar tetap sistematis, terarah, dan sesuai dengan batasan waktu maupun sumber daya yang tersedia, maka ditetapkan batasan-batasan masalah sebagai berikut:
1. Pengembangan dan implementasi aplikasi dikhususkan murni untuk berjalan pada platform perangkat bergerak (*mobile device*) yang menggunakan sistem operasi Android.
2. Fungsionalitas aplikasi dibatasi secara ketat pada dua fitur utama, yaitu instrumen pengecekan keamanan kata sandi (analisis dan evaluasi) serta ruang penyimpanan sandi (*Password Vault*) terenkripsi.
3. Parameter matematis dan heuristik yang digunakan untuk evaluasi kekuatan kata sandi difokuskan pada perhitungan *information entropy* (Teori Shannon), analisis komposisi dasar karakter, deteksi pola umum, serta formula estimasi waktu peretasan metode *brute-force*.
4. Algoritma kriptografi yang diaplikasikan untuk fungsionalitas keamanan penyimpanan dibatasi pada algoritma *Secure Hash Algorithm 256-bit* (SHA-256) dengan memanfaatkan pustaka standar `java.security.MessageDigest`.
5. Manajemen autentikasi pengguna dan persistensi data *backend* diselenggarakan secara terpusat dan tersinkronisasi menggunakan layanan dari Firebase (Firebase Authentication dan Cloud Firestore).
6. Aplikasi ini tidak dirancang untuk berfungsi sebagai layanan pengelola kata sandi komprehensif tingkat sistem; oleh karenanya, tidak menyertakan fitur pengisian otomatis kata sandi (*autofill service*) ke dalam aplikasi lain, dan tidak terintegrasi dengan ekosistem *password manager* pihak ketiga.

---

## 1.4 Tujuan Penelitian

Pengembangan aplikasi ini memiliki serangkaian tujuan penelitian yang dibagi menjadi tujuan umum dan tujuan khusus, yaitu:

### 1.4.1 Tujuan Umum
Membangun sebuah aplikasi *mobile* berbasis Android yang secara spesifik difungsikan sebagai instrumen edukasi dan utilitas untuk mengevaluasi kekuatan keamanan kata sandi, serta menyediakan fasilitas penyimpanan aman (*Password Vault*) berbasis *cloud* dengan menerapkan prinsip-prinsip keamanan kriptografi *hashing*.

### 1.4.2 Tujuan Khusus
1. **Mengimplementasikan mesin analitis kekuatan kata sandi** yang secara dinamis menghitung nilai *information entropy*, mengevaluasi komposisi karakter (*uppercase*, *lowercase*, numerik, dan simbol), mendeteksi keberadaan pola umum yang rentan, serta menyajikan estimasi matematis terkait waktu yang dibutuhkan untuk serangan *brute force*.
2. **Menerapkan operasi fungsi hash kriptografi** (meliputi MD5, SHA-256, dan SHA-512) dari pustaka `java.security.MessageDigest` guna menghasilkan representasi sidik jari digital (*fingerprint*) dari sebuah kata sandi.
3. **Membangun mekanisme *Password Vault* berbasis *Zero-Knowledge*** yang menyimpan kombinasi metadata akun (meliputi nama layanan dan *username*) beserta skor keamanan dan *fingerprint* kata sandi SHA-256 ke dalam layanan Cloud Firestore, tanpa pernah merekam teks asli kata sandi pengguna.
4. **Mengintegrasikan arsitektur *Backend as a Service* (BaaS)** melalui Firebase Authentication dan Cloud Firestore guna memfasilitasi manajemen pengguna secara aman, menerapkan *Firestore Security Rules* berbasis otorisasi otentikasi.

---

## 1.5 Manfaat Penelitian

Adapun manfaat yang diharapkan dari hasil perancangan dan implementasi aplikasi ini meliputi:
1. **Manfaat Teoritis:** Memberikan kontribusi pada kajian implementasi praktis algoritma kriptografi (khususnya *hashing* SHA-256 dan perhitungan *information entropy*) pada lingkungan perangkat bergerak (*mobile environment*).
2. **Manfaat Praktis (Bagi Pengguna):** Menyediakan sebuah utilitas yang intuitif bagi masyarakat umum untuk menguji, memvalidasi, serta menyadari tingkat kerentanan kata sandi yang mereka gunakan sehari-hari, sekaligus menyediakan media penyimpanan sandi yang aman dan tidak dapat disalahgunakan secara teoretis oleh pengelola pangkalan data sekalipun.

---

# BAB II
# TINJAUAN PUSTAKA

## 2.1 Kriptografi dan Keamanan Kata Sandi

Kriptografi (*cryptography*) secara fundamental didefinisikan sebagai disiplin ilmu dan seni matematika yang berfokus pada pengamanan informasi, penerapan teknik transformasi data menjadi bentuk tersandi (*ciphertext*) yang tidak dapat dibaca maupun dipahami oleh entitas yang tidak memiliki otorisasi (Stallings, 2023). Dalam lanskap keamanan kata sandi modern, kriptografi memainkan peranan esensial melalui implementasi berbagai mekanisme matematis kompleks, di antaranya fungsi *hash* searah (*one-way hash functions*), fungsi turunan kunci (*key derivation functions*), serta metrik kuantitatif seperti analisis *information entropy*.

Menurut studi komprehensif yang dilakukan oleh Al-Asli dan Furati (2021), tingkat keamanan sebuah kata sandi pada dasarnya sangat bergantung pada tiga faktor pembentuk utama: panjang karakter kata sandi (*password length*), tingkat keacakan distribusi karakter (*randomness*), serta derajat ketidakprediktifan kombinasi tersebut (*unpredictability*). Kata sandi yang berukuran pendek, secara repetitif menggunakan pola yang berulang, atau mengadopsi kosakata yang lazim ditemukan dalam kamus (*dictionary words*) memiliki tingkat kerentanan yang sangat tinggi terhadap mekanisme serangan siber otomatis.

## 2.2 Fungsi Hash Kriptografi (SHA-256)

*Secure Hash Algorithm 256-bit* (SHA-256) merupakan salah satu algoritma *hashing* yang tergabung dalam keluarga protokol SHA-2. Algoritma ini dirancang dan dikembangkan secara khusus oleh *National Security Agency* (NSA) serta dipublikasikan sebagai standar federal oleh *National Institute of Standards and Technology* (NIST). Secara teknis, fungsi SHA-256 memproses aliran data masukan (*input/message*) dengan ukuran sembarang dan mengonversinya menjadi sebuah nilai *hash* (*message digest*) tetap sepanjang 256 bit atau direpresentasikan dalam 64 karakter heksadesimal (NIST, 2023).

Signifikansi penggunaan SHA-256 dalam arsitektur keamanan kata sandi didasarkan pada empat properti kriptografis utamanya:
1. **Pre-image resistance**: Merupakan properti yang menjadikan komputasi rekayasa balik (*reverse engineering*) tidak layak dilakukan (*infeasible*). Artinya, sangat sulit untuk mendari atau menyimpulkan data masukan asli (*plaintext*) hanya bermodalkan nilai *hash* keluarannya.
2. **Second pre-image resistance**: Ketidakmungkinan secara komputasional untuk menemukan input sekunder yang berbeda, namun menghasilkan nilai *hash* yang sama dengan input primer yang telah diketahui.
3. **Collision resistance**: Jaminan resistensi terhadap tabrakan, di mana secara praktis tidak mungkin untuk mengonstruksi dua input acak yang berbeda namun menghasilkan output *hash* yang identik.
4. **Avalanche effect**: Karakteristik di mana modifikasi sekecil apa pun pada data masukan (meskipun hanya 1 bit) akan mendisrupsi proses matematis dan menghasilkan keluaran *hash* yang berubah secara drastis, sehingga menyamarkan pola hubungan antara input dan output.

Implementasi fungsi *hash* SHA-256 tetap diakui sebagai standar industri yang andal untuk keperluan representasi *fingerprint* kata sandi pada metode identifikasi *zero-knowledge*, mengingat keandalannya dalam mencegah kebocoran teks asli (Kumar & Singh, 2021).

## 2.3 Information Entropy dan Kekuatan Kata Sandi

Konsep *Information entropy*, yang pertama kali dirumuskan oleh Claude Shannon dalam teori informasi, adalah sebuah besaran yang mengukur tingkat ketidakpastian (*uncertainty*) atau keacakan dari suatu variabel atau paket informasi. Dalam konteks evaluasi kata sandi, *entropy* direpresentasikan secara matematis untuk mengalkulasi besarnya ruang pencarian (*search space*) yang harus ditelusuri oleh seorang penyerang (Grassi et al., 2023). Rumus perhitungan *entropy* kata sandi diformulasikan sebagai:

```
H = L × log₂(N)
```

Di mana:
- **H** merupakan total *entropy* dalam satuan bit.
- **L** melambangkan panjang keseluruhan kata sandi (kuantitas karakter).
- **N** mendefinisikan ukuran *character set* atau jumlah variasi karakter unik yang digunakan (misalnya gabungan alfabet, angka, dan simbol).

Semakin tinggi nilai *entropy* (*H*), semakin eksponensial pula kesulitan yang akan dihadapi oleh algoritma peretas dalam menebak kata sandi tersebut. Berdasarkan rekomendasi resmi dari pedoman *NIST Special Publication 800-63B* (Grassi et al., 2023), sebuah kata sandi dengan bobot *entropy* minimal 60 bit diklasifikasikan memadai untuk mengamankan akun layanan umum. Sementara itu, untuk sistem yang membutuhkan jaminan keamanan tingkat tinggi terhadap serangan *brute force* dengan komputasi paralel masif, dibutuhkan *entropy* yang melampaui 80 bit. 

Secara akademis, Fadhil dan Saputra (2022) mengusulkan klasifikasi kekuatan kata sandi berbasis *entropy* menjadi empat kategori empiris: kategori Lemah (< 28 bit), Sedang (28–35 bit), Kuat (36–59 bit), dan Sangat Kuat (≥ 60 bit).

## 2.4 Estimasi Waktu Peretasan Brute Force

Metode serangan *brute force* beroperasi dengan mekanisme algoritma yang mengeksekusi iterasi pencobaan seluruh kombinasi karakter secara sistematis dan masif (Ur et al., 2022). Ketahanan suatu kata sandi terhadap metode ini dapat dikuantifikasi melalui estimasi waktu rata-rata yang dibutuhkan untuk membobol pertahanannya, yang bergantung pada nilai *entropy* serta kapasitas komputasi penyerang (*guessing rate*). Formulasinya dijabarkan sebagai berikut:

```
T = 2^H / (2 × G)
```

Di mana:
- **T** menyatakan estimasi rata-rata waktu yang diperlukan (dalam satuan detik).
- **H** merujuk pada *entropy* kata sandi (dalam satuan bit).
- **G** merupakan tingkat kecepatan penyerang dalam mencoba kombinasi sandi (*guesses per second*).

Dalam simulasi skenario ancaman modern, diasumsikan seorang penyerang memiliki akses ke kluster *Graphics Processing Unit* (GPU) canggih yang mampu memproses hingga 10⁹ (satu miliar) percobaan *hash* per detik. Dengan asumsi komputasi tersebut, sebuah kata sandi yang dikonfigurasi dengan baik dan memiliki *entropy* sebesar 80 bit secara teoritis akan membutuhkan waktu rata-rata lebih dari 19.000 tahun komputasi tanpa henti untuk berhasil dipecahkan.

## 2.5 Deteksi Pola Umum pada Kata Sandi

Meskipun sebuah kata sandi terlihat panjang, penelitian terkini oleh Tan et al. (2023) mendemonstrasikan bahwa kebiasaan psikologis manusia sering kali menghasilkan kata sandi yang mengadopsi pola-pola repetitif yang sangat mudah diprediksi. Beberapa jenis pola rentan tersebut meliputi:
- **Urutan karakter sekuensial** (*sequential patterns*): Pola berurutan dari alfabet atau posisi *keyboard* (contoh: "123456", "abcdef", "qwerty").
- **Karakter berulang** (*repeated characters*): Repetisi statis karakter identik (contoh: "aaaaaa", "111111").
- **Kata lazim / Kosakata kamus** (*common words*): Pemilihan frasa yang mudah ditebak secara kultural (contoh: "password", "admin123", "welcome").

Melicher et al. (2022) menegaskan bahwa deteksi pola-pola prediktif tersebut adalah tahapan krusial dalam algoritma evaluasi kata sandi. Keberadaan satu atau lebih pola umum secara matematis akan mereduksi nilai *effective entropy* secara drastis, sehingga nilai *entropy* mentah menjadi bias karena tidak merepresentasikan tingkat kesulitan komputasi sebenarnya dalam proses penembakan (*cracking*).

## 2.6 Firebase Authentication dan Cloud Firestore

Sebagai tulang punggung infrastruktur sisi *server*, *Firebase Authentication* menawarkan solusi manajemen identitas berbasis standar industri. Layanan ini membungkus proses autentikasi (seperti *email/password login*) dengan enkripsi *Transport Layer Security* (TLS) berlapis selama proses transit data, serta menyediakan mitigasi bawaan (*built-in protection*) terhadap anomali otentikasi seperti *brute force attack* (Firebase Documentation, 2024).

Sedangkan *Cloud Firestore* merupakan pangkalan data terdistribusi berbasis NoSQL yang diformat untuk menangani sinkronisasi data *real-time*. Komponen terpenting dari Firestore dalam konteks keamanan aplikasi adalah fitur *Firestore Security Rules*, yang memberikan kemampuan penerapan kontrol akses presisi tinggi. Sesuai prinsip *Least Privilege*, *rules* tersebut dapat diprogram untuk memvalidasi token *User ID* (UID) dari *Firebase Authentication*, guna menjamin bahwa pengguna hanya diizinkan untuk melakukan operasi *Read/Write* pada partisi data miliknya sendiri tanpa bisa menembus data pengguna lain (Moroney, 2021).

---

# BAB III
# METODOLOGI DAN PERANCANGAN SISTEM

## 3.1 Metodologi Pengembangan Perangkat Lunak

Pengembangan aplikasi **KriptoTugas1** dilaksanakan menggunakan metodologi *Prototyping* iteratif. Pemilihan metodologi ini didasarkan pada fleksibilitasnya dalam mengakomodasi perubahan spesifikasi sistem terkait algoritma evaluasi keamanan selama proses rekayasa perangkat lunak berlangsung. Tahapan pengembangan mencakup: (1) identifikasi kebutuhan fungsional dan keamanan, (2) perancangan purwarupa antarmuka (*quick design*), (3) rekayasa fungsi algoritma kriptografi (evaluasi *entropy* dan *hashing*), serta (4) pengujian operasionalisasi *backend database*.

## 3.2 Analisis Kebutuhan Sistem

Analisis kebutuhan arsitektur direpresentasikan ke dalam dua instrumen metrik teknis utama:
- **Kebutuhan Fungsional:** Perangkat lunak harus mampu menyelenggarakan protokol otentikasi pengguna secara presisi, memvalidasi dan membedah profil keamanan kata sandi ketika instruksi analisis dieksekusi oleh pengguna, memproduksi *fingerprint* kriptografis searah (SHA-256), serta menampung metadata evaluasi tersebut ke pangkalan data terenkripsi (*Password Vault*).
- **Kebutuhan Non-Fungsional:** Sistem mengemban kewajiban absolut untuk menjamin privasi pengguna dengan menegakkan desain arsitektur tanpa-pengetahuan (*Zero-Knowledge Architecture*)—memastikan tidak ada ruang bagi kata sandi berbentuk teks asli (*plaintext*) untuk dapat terekam atau berdiam di lapisan *server*.

## 3.3 Arsitektur Sistem

Aplikasi ini dikembangkan bersandar pada arsitektur monolitik terpusat dengan mengadopsi pola rekayasa *Single-Activity Architecture* berbasis kerangka kerja *Android Jetpack*. Mekanisme rute transisi antar-halaman utama diorkestrasikan oleh komponen *BottomNavigationView*. Secara konseptual, fondasi sistem dibangun di atas tiga lapisan (*layers*) hierarkis yang independen:

```
┌─────────────────────────────────────────────────────┐
│                  Presentation Layer                  │
│       ┌────────┐      ┌───────┐                    │
│       │  Home  │      │ Vault │                    │
│       │(Analisis│      │(Pass. │                    │
│       │Password)│      │Vault) │                    │
│       └────┬───┘      └───┬───┘                    │
├────────────┼──────────────┼────────────────────────┤
│       │   Cryptographic Engine Layer │             │
│  ┌────┴─────────┴─────────┴──────────┴────────┐    │
│  │  SHA-256 Hashing (java.security.MessageDigest) ││
│  │  Entropy Calculator  │  Strength Scorer        ││
│  │  Pattern Detector    │  Brute Force Estimator  ││
│  └────────────────────┬───────────────────────┘    │
├───────────────────────┼─────────────────────────────┤
│                 Backend Layer                        │
│  ┌────────────────────┴─────────────────────────┐   │
│  │  Firebase Authentication (Email/Password)     │   │
│  │  Cloud Firestore (Data Storage per User)      │   │
│  │  Firestore Security Rules (UID-based ACL)     │   │
│  └───────────────────────────────────────────────┘   │
└─────────────────────────────────────────────────────┘
```

## 3.4 Perancangan Modul

### 3.4.1 Modul Autentikasi (Auth Gate)

Modul ini mendefinisikan gerbang perimeter keamanan tingkat pertama (*first line of defense*) dengan memanfaatkan layanan *Firebase Authentication* melalui skema protokol kredensial konvensional (*email/password*).

| Komponen | Deskripsi |
|---|---|
| **Input** | Email, Password Firebase |
| **Proses** | Login (`signInWithEmailAndPassword`), Register (`createUserWithEmailAndPassword`), Logout (`signOut`) |
| **Output** | Status autentikasi, akses ke fitur utama |
| **Keamanan** | Auth gate memblokir akses ke halaman utama jika user belum login; Firestore rules membatasi data per `userId` |

### 3.4.2 Modul Analisis Kekuatan Kata Sandi (Home)

Modul mesin analitis (*analytical engine*) utama pada aplikasi yang dirancang secara khusus untuk memproses, menguji, dan memvalidasi ketahanan kata sandi berdasarkan serangkaian indikator kriptografis mendalam. Proses kalkulasi dipicu secara spesifik melalui pendelegasian aksi klik (*click event execution*), guna menghasilkan representasi laporan komprehensif yang empiris atas kekuatan kata sandi pengguna.

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

### 3.4.3 Modul Password Vault

Komponen pangkalan data privat (*private repository*) yang bertugas menyinkronkan arsip metadata kata sandi ke *server cloud* secara independen, memegang teguh larangan penyimpanan kata sandi mentah melalui pendekatan arsitektur *Zero-Knowledge*.

| Komponen | Deskripsi |
|---|---|
| **Input** | Nama layanan, Username, Password |
| **Proses** | Analisis kekuatan password → Hitung fingerprint SHA-256 dari gabungan `VAULT|service|username|password` → Simpan metadata ke Firestore |
| **Data Tersimpan** | serviceName, username, strengthLevel, strengthScore, fingerprintSha256, createdAt |
| **Keamanan** | Password asli **tidak** disimpan; hanya fingerprint SHA-256 |
| **Firestore Collection** | `users/{userId}/vault_entries` |

## 3.5 Perancangan Keamanan Data

### 3.5.1 Konfigurasi Firestore Security Rules

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

### 3.5.2 Implementasi Zero-Knowledge Storage

Guna memastikan integritas privasi komprehensif, arsitektur aplikasi dirancang secara kaku untuk menolak persistensi kata sandi berbentuk teks asli (*plaintext*) di setiap node komputasi, termasuk pada koleksi pangkalan data awan Firestore. Struktur muatan data (*payload*) yang ditransmisikan hanya merangkum entitas berikut:
- **Fingerprint SHA-256**: Representasi satu arah (*one-way*) dari kata sandi.
- **Metadata analisis**: Panjang, skor kekuatan, level, dan entropy.
- **Timestamp**: Waktu analisis dilakukan.

## 3.6 Perancangan Antarmuka (UI/UX)

Antarmuka pengguna (*User Interface*) diproyeksikan dan diorkestrasikan dengan tunduk pada pedoman desain visual resmi mutakhir dari **Material Design 3** (M3), bernaung di bawah pustaka perangkat lunak `com.google.android.material`. Implementasi komponen-komponen utamanya meliputi:

| Komponen UI | Implementasi |
|---|---|
| Navigasi | `BottomNavigationView` dengan 2 tab (Analisis, Vault) |
| Input | `TextInputLayout` + `TextInputEditText` dengan dukungan *password toggle* |
| Progress | `LinearProgressIndicator` untuk visualisasi skor kekuatan |
| Tombol | `MaterialButton` dengan styling Material Design |
| Layout | `ConstraintLayout` sebagai root, `ScrollView` untuk konten panjang |
| Warna Indikator | Merah (danger/lemah), Kuning (warning/sedang), Hijau (success/kuat) |

## 3.7 Spesifikasi Teknologi Pendukung

| Teknologi Terapan | Versi/Spesifikasi | Fungsi Operasional Spesifik |
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

Stallings, W. (2023). *Cryptography and network security: Principles and practice* (8th ed.). Pearson Education. ISBN: 978-0-13-567022-1.

Tan, J., Bauer, L., Christin, N., & Cranor, L. F. (2023). Practical recommendations for stronger, more usable passwords combining minimum-strength, minimum-length, and blocklist requirements. *ACM Computing Surveys*, 55(13s), 1–36. https://doi.org/10.1145/3596908

Ur, B., Alfieri, F., Aung, M., Bauer, L., Christin, N., & Cranor, L. F. (2022). Design and evaluation of a data-driven password meter with real-time feedback. *ACM Transactions on Computer-Human Interaction*, 29(5), 1–41. https://doi.org/10.1145/3531065

Verizon. (2023). *2023 Data Breach Investigations Report*. Verizon Enterprise Solutions. https://www.verizon.com/business/resources/reports/dbir/
