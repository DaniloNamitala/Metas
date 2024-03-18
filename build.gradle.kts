plugins {
    id("com.android.application") version "8.2.2" apply false
    id("org.jetbrains.kotlin.android") version "1.9.22" apply false
    id("com.google.devtools.ksp") version "1.8.10-1.0.9" apply false
    id("androidx.room") version "2.6.1" apply false
    id("org.jlleitschuh.gradle.ktlint") version "11.1.0"
}

buildscript {
    repositories {
        maven("https://plugins.gradle.org/m2/")
    }
    dependencies {
        classpath("org.jlleitschuh.gradle:ktlint-gradle:11.1.0")
    }
}

apply(plugin = "org.jlleitschuh.gradle.ktlint")
