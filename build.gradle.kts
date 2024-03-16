// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    dependencies {
        classpath("com.diffplug.spotless:spotless-plugin-gradle:6.19.0")
    }
}

plugins {
    id("com.android.application") version "8.2.2" apply false
    id("org.jetbrains.kotlin.android") version "1.9.22" apply false
    id("com.google.devtools.ksp") version "1.8.10-1.0.9" apply false
    id("androidx.room") version "2.6.1" apply false
    id("com.diffplug.spotless") version "6.19.0" apply false
}