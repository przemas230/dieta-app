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

// jvmToolchain(21) pinned explicitly: without it, compileJava targets
// whatever JDK actually runs Gradle (e.g. Android Studio's bundled JBR),
// while the Kotlin plugin caps its own target at the newest JDK it
// supports — on newer bundled JDKs (25) those two land on different
// versions and compileKotlin fails with "Inconsistent JVM Target
// Compatibility". JDK 21 is broadly available (bundled with recent
// Android Studio / IntelliJ) so this shouldn't require downloading one.
kotlin {
    jvmToolchain(21)
}

tasks.test {
    useJUnitPlatform()
}
