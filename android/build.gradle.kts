// Top-level build file — plugin versions declared here, applied per-module below.
plugins {
    id("com.android.application") version "9.3.1" apply false
    id("org.jetbrains.kotlin.android") version "2.2.10" apply false
    id("org.jetbrains.kotlin.plugin.compose") version "2.2.10" apply false
    id("com.google.gms.google-services") version "4.4.2" apply false
    // Used by the plain :logic module (see android/logic/build.gradle.kts) —
    // same Kotlin version as the rest of the project.
    id("org.jetbrains.kotlin.jvm") version "2.1.10" apply false
}
