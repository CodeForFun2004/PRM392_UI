plugins {
    id("com.android.application")
    id("com.google.gms.google-services") // bạn đã thêm version ở root build.gradle.kts
}

android {
    namespace = "com.example.chillcup02_ui"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.chillcup02_ui"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        multiDexEnabled = true

        // ✅ Đặt ĐÚNG CHỖ ở trong defaultConfig
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildFeatures {
        viewBinding = true
        buildConfig = true
    }

    buildTypes {
        getByName("debug") {
            isMinifyEnabled = false
        }
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        // Nếu dự án/JDK của bạn là 11 thì giữ nguyên; nếu dùng JDK 17 thì đổi sang VERSION_17
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    // Flavors (Kotlin DSL)
    flavorDimensions += "env"
    productFlavors {
        create("dev") {
            dimension = "env"
            applicationIdSuffix = ".dev"
            resValue("string", "app_name", "ChillCup (Dev)")
            // For emulator to reach host machine use 10.0.2.2:8080 where The-Chill-Cup-API runs
            buildConfigField("String", "BASE_URL", "\"http://10.0.2.2:8080/\"")
        }
        create("prod") {
            dimension = "env"
            resValue("string", "app_name", "ChillCup")
            buildConfigField("String", "BASE_URL", "\"https://api.yourdomain.com/\"")
        }
    }
}

dependencies {
    // libs.* dùng Version Catalog — giữ nguyên nếu bạn đã cấu hình libs.versions.toml
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)

    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)

    // Firebase BOM (Kotlin DSL)
    implementation(platform("com.google.firebase:firebase-bom:34.5.0"))
    implementation("com.google.firebase:firebase-auth")
    implementation("com.google.firebase:firebase-analytics")
    // implementation("com.google.firebase:firebase-messaging") // nếu dùng
    
    // Google Sign-In
    implementation("com.google.android.gms:play-services-auth:21.2.0")

    // Network
    implementation("com.squareup.retrofit2:retrofit:2.11.0")
    implementation("com.squareup.retrofit2:converter-gson:2.11.0")
    implementation("com.squareup.okhttp3:okhttp:4.12.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")

    // Room (Java project => annotationProcessor; nếu dùng Kotlin code thì thay bằng kapt)
    implementation("androidx.room:room-runtime:2.6.1")
    annotationProcessor("androidx.room:room-compiler:2.6.1")

    // Lifecycle
    implementation("androidx.lifecycle:lifecycle-livedata:2.8.6")
    implementation("androidx.lifecycle:lifecycle-viewmodel:2.8.6")
    
    // Fragment
    implementation("androidx.fragment:fragment:1.8.6")

    // UI (Glide)
    implementation("com.github.bumptech.glide:glide:4.16.0")
    annotationProcessor("com.github.bumptech.glide:compiler:4.16.0")
}
