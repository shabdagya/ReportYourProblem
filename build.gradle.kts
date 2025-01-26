// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("com.android.application") version "8.2.0-rc03" apply false
    id("org.jetbrains.kotlin.android") version "1.9.10" apply false
    id("com.google.gms.google-services") version "4.4.2" apply false
}
configurations.all {
    resolutionStrategy.eachDependency {
        if (requested.group == "org.jetbrains.kotlin") {
            useVersion ("1.9.10") // Match your Kotlin version
        }
    }
}
buildscript {
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath ("com.google.gms:google-services:4.3.15")
        classpath ("org.jetbrains.kotlin:kotlin-gradle-plugin:2.1.0")// Firebase plugin
    }
}
