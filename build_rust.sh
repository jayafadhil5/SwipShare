#!/usr/bin/env bash
# build_rust.sh — Build Rust core library dan copy ke jniLibs
#
# Prasyarat:
#   1. Rust terinstall: pkg install rust  (Termux) / rustup
#   2. cargo-ndk: cargo install cargo-ndk
#   3. NDK terinstall dan ANDROID_NDK_HOME ter-set
#
# Penggunaan:
#   ./build_rust.sh [debug|release]   default: release
#
# Output: app/src/main/jniLibs/<abi>/libswiftdrop_core.so

set -euo pipefail

PROFILE=${1:-release}
RUST_DIR="$(cd "$(dirname "$0")/rust" && pwd)"
OUT_DIR="$(cd "$(dirname "$0")/app/src/main/jniLibs" && pwd)"

TARGETS=(
    "aarch64-linux-android"
    "armv7-linux-androideabi"
    "x86_64-linux-android"
)

ABI_MAP=(
    "aarch64-linux-android:arm64-v8a"
    "armv7-linux-androideabi:armeabi-v7a"
    "x86_64-linux-android:x86_64"
)

echo "=== SwiftDrop Rust Core Build ($PROFILE) ==="
echo "NDK: ${ANDROID_NDK_HOME:-tidak diset — cargo-ndk akan cari sendiri}"

cd "$RUST_DIR"

# Tambah targets bila belum ada
for TARGET in "${TARGETS[@]}"; do
    rustup target add "$TARGET" 2>/dev/null || true
done

for MAPPING in "${ABI_MAP[@]}"; do
    TARGET="${MAPPING%%:*}"
    ABI="${MAPPING##*:}"
    ABI_OUT="$OUT_DIR/$ABI"
    mkdir -p "$ABI_OUT"

    echo ""
    echo "→ Build $TARGET ($ABI)..."
    if [ "$PROFILE" = "release" ]; then
        cargo ndk -t "$TARGET" build --release
        SO_PATH="$RUST_DIR/target/$TARGET/release/libswiftdrop_core.so"
    else
        cargo ndk -t "$TARGET" build
        SO_PATH="$RUST_DIR/target/$TARGET/debug/libswiftdrop_core.so"
    fi

    if [ -f "$SO_PATH" ]; then
        cp "$SO_PATH" "$ABI_OUT/libswiftdrop_core.so"
        SIZE=$(du -h "$ABI_OUT/libswiftdrop_core.so" | cut -f1)
        echo "  ✓ $ABI_OUT/libswiftdrop_core.so ($SIZE)"
    else
        echo "  ✗ Build gagal untuk $TARGET"
        exit 1
    fi
done

echo ""
echo "=== Build selesai ==="
echo "Jalankan './gradlew assembleDebug' atau 'assembleRelease' untuk build APK."
