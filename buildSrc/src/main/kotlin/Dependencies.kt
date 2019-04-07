@file:Suppress("unused", "SpellCheckingInspection")

import org.gradle.api.JavaVersion

object Config {
    const val minSdk = 21
    const val compileSdk = 28
    const val targetSdk = 28
    val javaVersion = JavaVersion.VERSION_1_8
    const val buildTools = "28.0.3"
}

object Versions {
    const val coroutines = "1.1.1"
    const val glide = "4.9.0"
    const val gradleAndroid = "3.3.2"
    const val kodein = "6.1.0"
    const val kotlin = "1.3.21"
    const val sqlDelight = "1.1.1"
}

object Deps {
    const val coroutinesCore = "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.coroutines}"
    const val coroutinesAndroid = "org.jetbrains.kotlinx:kotlinx-coroutines-android:${Versions.coroutines}"
    const val glide = "com.github.bumptech.glide:glide:${Versions.glide}"
    const val glideCompiler = "com.github.bumptech.glide:compiler:${Versions.glide}"
    const val kodein = "org.kodein.di:kodein-di-generic-jvm:${Versions.kodein}"
    const val kodeinAndroid = "org.kodein.di:kodein-di-framework-android-x:${Versions.kodein}"
    const val kotlin = "org.jetbrains.kotlin:kotlin-stdlib-jdk7:${Versions.kotlin}"
    const val sqlDelight = "com.squareup.sqldelight:android-driver:${Versions.sqlDelight}"

    const val tools_gradleAndroid = "com.android.tools.build:gradle:${Versions.gradleAndroid}"
    const val tools_kotlin = "org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions.kotlin}"
    const val tools_sqlDelight = "com.squareup.sqldelight:gradle-plugin:${Versions.sqlDelight}"
}