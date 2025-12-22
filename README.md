Tentu, ini adalah file `README.md` yang disusun khusus untuk proyek **SholatApp** Anda. Isinya disesuaikan agar terlihat profesional, mengikuti struktur "Grand Orion" yang Anda unggah, dan menonjolkan kriteria penilaian UAP Pemrograman Lanjut 2025.

---

# SholatApp - Aplikasi Jadwal Sholat & Imsyakiah

**SholatApp** adalah aplikasi desktop berbasis Java Swing yang dirancang untuk membantu pengelolaan data kota dan pengecekan jadwal sholat serta imsyakiah secara luring (offline). Proyek ini menunjukkan penerapan konsep pemrograman berorientasi objek (OOP) yang dikombinasikan dengan manajemen file lokal.

## Fitur Utama

1. **Manajemen Kota (CRUD)**
* Pengguna dapat menambah, melihat, dan menghapus daftar kota yang tersimpan di dalam sistem.
* Data bersifat permanen karena tersimpan langsung ke dalam file `kota.csv`.


2. **Cek Jadwal Sholat Dinamis**
* Fitur pencarian jadwal berdasarkan kota yang dipilih melalui *combo box*.
* Menampilkan detail waktu ibadah (Imsak, Subuh, dsb) secara otomatis dari database file `jadwal.csv`.


3. **Antarmuka Multi-Halaman (CardLayout)**
* Navigasi yang mulus antara menu utama, panel manajemen kota, dan panel jadwal tanpa membuka jendela baru.



## Implementasi Modul (Kriteria UAP)

Aplikasi ini telah memenuhi kriteria penilaian berikut:

* **Modul 1 & 2 (Refactoring & Correctness)**: Pemisahan logika antarmuka menggunakan metode berbasis panel dan penanganan input yang divalidasi dengan `JOptionPane`.
* **Modul 4 (API & Collections)**: Menggunakan `ArrayList` untuk manajemen data sementara di memori dan `DefaultTableModel` untuk pengolahan data tabel.
* **Modul 5 (File Handling)**: Implementasi penyimpanan data menggunakan `BufferedReader`, `PrintWriter`, dan `FileWriter` untuk berinteraksi dengan file `.csv`.
* **Modul 6 (GUI)**: Menggunakan komponen **Java Swing** seperti `JFrame`, `JPanel`, `JButton`, `JTable`, dan `CardLayout` untuk pengalaman pengguna yang interaktif.

## Struktur Proyek

```text
SholatApp/
├── kota.csv                # Database lokal daftar kota
├── jadwal.csv              # Database lokal jadwal sholat
├── README.md               # Dokumentasi Proyek
└── src/
    ├── App.java            # Main entry point aplikasi
    └── view/
        └── SholatApp.java   # Logika GUI dan File Handling

```

## Cara Menjalankan Aplikasi

1. **Persiapan File**: Pastikan file `kota.csv` dan `jadwal.csv` berada di folder yang sama dengan file `.java` atau di root project.
2. **Format CSV**:
* `kota.csv`: `ID,NamaKota` (Contoh: `1,Jakarta`)
* `jadwal.csv`: `Kota,Waktu,Jam` (Contoh: `Jakarta,Subuh,04:20`)


3. **Kompilasi**:
```bash
javac src/App.java src/view/SholatApp.java

```


4. **Jalankan**:
```bash
java src/App

```



---

**Dikembangkan oleh:**

* Rizal al syahril - 202410370110105
* Fathurrahman umar - 202410370110072

---


