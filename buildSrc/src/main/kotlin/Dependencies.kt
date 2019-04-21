@file:Suppress("unused", "SpellCheckingInspection")

import org.gradle.api.JavaVersion

object Config {
    const val minSdk = 21
    const val compileSdk = 28
    const val targetSdk = 28
    const val buildTools = "28.0.3"
    val javaVersion = JavaVersion.VERSION_1_8
}

object Versions {
    const val androidxTesting = "1.1.1"
    const val coroutines = "1.1.1"
    const val espresso = "3.1.0"
    const val glide = "4.9.0"
    const val gradleAndroid = "3.3.2"
    const val junit = "4.12"
    const val kodein = "6.1.0"
    const val kotlin = "1.3.21"
    const val kotpref = "2.7.0"
    const val materialPopupMenu = "a6f06e29de"
    const val okhttp = "3.14.1"
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
    const val kotpref = "com.chibatching.kotpref:kotpref:${Versions.kotpref}"
    const val materialPopupMenu = "com.github.zawadz88:MaterialPopupMenu:${Versions.materialPopupMenu}"
    const val okhttp = "com.squareup.okhttp3:okhttp:${Versions.okhttp}"
    const val sqlDelight = "com.squareup.sqldelight:android-driver:${Versions.sqlDelight}"

    const val tools_gradleAndroid = "com.android.tools.build:gradle:${Versions.gradleAndroid}"
    const val tools_kotlin = "org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions.kotlin}"
    const val tools_sqlDelight = "com.squareup.sqldelight:gradle-plugin:${Versions.sqlDelight}"

    const val test_androidxRunner = "androidx.test:runner:${Versions.androidxTesting}"
    const val test_espresso = "androidx.test.espresso:espresso-core:${Versions.espresso}"
    const val test_junit = "junit:junit:${Versions.junit}"
}
