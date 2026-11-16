plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.plugin.compose")
}

android {
    namespace = "app.swiftdrop"
    compileSdk = 35

    defaultConfig {
        applicationId = "app.swiftdrop"
        minSdk = 26
        targetSdk = 35
        versionCode = 3
        versionName = "0.3.0"

        // ABI splits: sertakan semua arch yang didukung Rust + cargo-ndk
        ndk {
            abiFilters += listOf("arm64-v8a", "armeabi-v7a", "x86_64")
        }

        // Deklarasi prefetch font Inter agar tersedia offline lebih cepat
        manifestPlaceholders["fontManifestMetaData"] = "inter_fontfamily"
    }

    buildFeatures {
        compose = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }

    packaging {
        resources {
            excludes += "/META-INF/versions/9/OSGI-INF/MANIFEST.MF"
        }
        // .so dari Rust sudah ada di jniLibs/; pastikan tidak di-merge ganda
        jniLibs {
            useLegacyPackaging = false
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            // Untuk production: aktifkan R8 dan ProGuard rules
            // isMinifyEnabled = true
            // proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
        debug {
            // Rust .so di-copy dari build/rust ke jniLibs saat debug build
            // Jalankan ./build_rust.sh debug terlebih dahulu
        }
    }

    // Lokasi jniLibs yang di-generate cargo-ndk
    sourceSets["main"].jniLibs.srcDirs("src/main/jniLibs")
}

dependencies {
    implementation(platform("androidx.compose:compose-bom:2024.09.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.compose.animation:animation")
    implementation("androidx.activity:activity-compose:1.9.2")
    implementation("androidx.core:core-ktx:1.13.1")
    implementation("androidx.documentfile:documentfile:1.0.1")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.8.1")
    // TLS identity: sertifikat self-signed EC P-256
    implementation("org.bouncycastle:bcpkix-jdk18on:1.78.1")
    // QR code generate & scan
    implementation("com.google.zxing:core:3.5.3")
    // Kamera pemindai QR
    implementation("androidx.camera:camera-core:1.3.4")
    implementation("androidx.camera:camera-camera2:1.3.4")
    implementation("androidx.camera:camera-lifecycle:1.3.4")
    implementation("androidx.camera:camera-view:1.3.4")
    implementation("androidx.compose.material:material-icons-core")
    // Downloadable fonts support
    implementation("androidx.core:core-ktx:1.13.1")

    testImplementation("junit:junit:4.13.2")
}
