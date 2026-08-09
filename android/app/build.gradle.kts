plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.plugin.compose")
}

// google-services.json isn't checked in yet (you add it yourself from the
// Firebase console — see android/README.md). Applying the plugin only when
// the file exists means the app still builds and runs today (recipe list,
// no Firebase calls yet) without forcing that step first.
val hasGoogleServicesJson = file("google-services.json").exists()
if (hasGoogleServicesJson) {
    apply(plugin = "com.google.gms.google-services")
}

android {
    namespace = "com.przemas230.dietaapp"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.przemas230.dietaapp"
        minSdk = 26
        targetSdk = 34
        versionCode = 9
        versionName = "0.1.8"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }

    buildFeatures {
        compose = true
    }
}

dependencies {
    // Pure Kotlin/JVM business logic (recipe filtering, pantry/shopping map
    // operations) + the data classes they operate on — kept in a separate
    // module specifically so it can be unit-tested without pulling in
    // Android/AndroidX (see android/logic/build.gradle.kts and
    // android/README.md "Testy automatyczne").
    implementation(project(":logic"))

    implementation(platform("androidx.compose:compose-bom:2024.10.00"))
    implementation("androidx.activity:activity-compose:1.9.3")
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.6")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.6")
    implementation("androidx.navigation:navigation-compose:2.8.2")
    // Extended icon set (TrendingUp, Inventory2, Restaurant aren't in the
    // small default set) — comes from the same Compose BOM above, so its
    // version always matches the rest of Compose automatically.
    implementation("androidx.compose.material:material-icons-extended")

    implementation(platform("com.google.firebase:firebase-bom:33.4.0"))
    implementation("com.google.firebase:firebase-auth-ktx")
    implementation("com.google.firebase:firebase-firestore-ktx")
    // .await() on Firebase Tasks (anonymous sign-in, Firestore get/set) inside
    // coroutines — used by SettingsScreen's Firebase connectivity test.
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-play-services:1.9.0")

    debugImplementation("androidx.compose.ui:ui-tooling")
}
