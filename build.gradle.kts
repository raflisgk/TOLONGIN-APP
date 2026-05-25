buildscript {
    repositories { google(); mavenCentral() }
    dependencies {
        classpath("com.android.tools.build:gradle:8.1.0")
    }
}
plugins {
    id("com.android.application") version "8.2.2" apply false
    // GANTI baris di bawah ini jadi 1.9.0
    id("org.jetbrains.kotlin.android") version "1.9.0" apply false
}
