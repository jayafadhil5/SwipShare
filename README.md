# SwiftDrop (v0.3.0)

SwiftDrop — Pengiriman file & teks langsung lokal via Wi-Fi berkecepatan tinggi dengan backend hybrid Kotlin + Rust.

## Arsitektur & Update Baru

1. **Rust Core (`rust/`)**:
   - `crypto.rs`: SHA-256 murni, sidik jari 128-bit, SAS verification code 6-digit.
   - `frames.rs`: Framing biner berkecepatan tinggi & zero-copy.
   - `transfer_io.rs`: Sliding window speed EWMA & estimasi waktu selesai (ETA).
   - `util.rs`: Pembersihan nama file, validasi IPv4, dan parser QR.

2. **Kotlin FFI & Fallback (`app/src/main/java/app/swiftdrop/core/RustBridge.kt`)**:
   - Terhubung mulus via JNI (`libswiftdrop_core.so`).
   - Kompatibel 100% dengan Kotlin murni jika native library belum dikompilasi.

3. **Fitur Baru**:
   - **Kirim Teks / Clipboard**: Kirim teks instan antar perangkat via saluran TLS terenkripsi.
   - **Indikator ETA & Kecepatan EWMA**: Estimasi sisa waktu real-time.
   - **Notifikasi Transfer Interaktif**: Tap notifikasi langsung membuka tampilan progres transfer aktif.

4. **Redesign Tampilan ("Precision Minimal")**:
   - Palet baru warna Indigo Modern (`#4F46E5`), surface warm, dark mode adaptif.
   - Tipografi Inter dengan skala hierarki tajam.
   - Bottom Navigation Bar (Beranda, Riwayat, Pengaturan).
   - Animasi progres transfer dinamis & kartu riwayat transfer cepat di beranda.

## Build Rust Core
```bash
./build_rust.sh release
```
