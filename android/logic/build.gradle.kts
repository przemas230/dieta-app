// Plain Kotlin/JVM module — deliberately zero Android/AndroidX dependencies,
// so its Gradle build only needs mavenCentral()/gradlePluginPortal() and can
// be genuinely compiled and tested in environments where dl.google.com
// (Android's Maven repo) is unreachable. See android/README.md "Testy
// automatyczne" for how this fits into the rest of the app.
plugins {
    id("org.jetbrains.kotlin.jvm")
}

// Repositories are centrally declared in settings.gradle.kts
// (dependencyResolutionManagement) — this module doesn't need google(),
// only what's already there (mavenCentral()), it just doesn't declare its
// own repositories{} block since that's disallowed by FAIL_ON_PROJECT_REPOS.

dependencies {
    testImplementation(kotlin("test"))
    testImplementation("org.junit.jupiter:junit-jupiter:5.11.3")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

// No explicit jvmToolchain() here on purpose — this compiles with whatever
// JDK actually runs Gradle (Android Studio's bundled JDK when opened there),
// so there's nothing extra to auto-provision/download if that JDK's version
// doesn't match a hardcoded number.

tasks.test {
    useJUnitPlatform()
}
