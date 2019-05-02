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
    const val androidxLegacy = "1.0.0"
    const val androidxTesting = "1.1.1"
    const val androidxAppCompat = "1.0.2"
    const val constraintLayout = "2.0.0-alpha3"
    const val coreTesting = "2.0.0"
    const val coroutines = "1.1.1"
    const val crux = "2.0.2"
    const val espresso = "3.1.0"
    const val glide = "4.9.0"
    const val gradleAndroid = "3.3.2"
    const val jsoup = "1.11.3"
    const val junit = "4.12"
    const val kodein = "6.1.0"
    const val kotlin = "1.3.21"
    const val kotpref = "2.7.0"
    const val ktlint = "0.32.0"
    const val ktx = "1.1.0-alpha05"
    const val lifecycle = "2.0.0"
    const val lifecycleViewModel = "2.1.0-alpha03"
    const val material = "1.1.0-alpha05"
    const val materialPopupMenu = "a6f06e29de"
    const val okhttp = "3.14.1"
    const val recyclerView = "1.0.0"
    const val recyclerViewUtils = "c6e595ab0f"
    const val sqlDelight = "1.1.1"
    const val timber = "4.7.1"
    const val work = "2.0.0"
}

object Deps {
    const val androidxLegacy = "androidx.legacy:legacy-support-v4:${Versions.androidxLegacy}"
    const val androidxAppCompat = "androidx.appcompat:appcompat:${Versions.androidxAppCompat}"
    const val constraintLayout = "androidx.constraintlayout:constraintlayout:${Versions.constraintLayout}"
    const val coroutinesAndroid = "org.jetbrains.kotlinx:kotlinx-coroutines-android:${Versions.coroutines}"
    const val coroutinesCore = "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.coroutines}"
    const val crux = "com.github.chimbori:crux:${Versions.crux}"
    const val glide = "com.github.bumptech.glide:glide:${Versions.glide}"
    const val glideCompiler = "com.github.bumptech.glide:compiler:${Versions.glide}"
    const val jsoup = "org.jsoup:jsoup:${Versions.jsoup}"
    const val kodein = "org.kodein.di:kodein-di-generic-jvm:${Versions.kodein}"
    const val kodeinAndroid = "org.kodein.di:kodein-di-framework-android-x:${Versions.kodein}"
    const val kodeinConfigurable = "org.kodein.di:kodein-di-conf-jvm:${Versions.kodein}"
    const val kotlin = "org.jetbrains.kotlin:kotlin-stdlib-jdk7:${Versions.kotlin}"
    const val kotpref = "com.chibatching.kotpref:kotpref:${Versions.kotpref}"
    const val ktx = "androidx.core:core-ktx:${Versions.ktx}"
    const val lifecycle = "androidx.lifecycle:lifecycle-extensions:${Versions.lifecycle}"
    const val lifecycleViewModel = "androidx.lifecycle:lifecycle-viewmodel-ktx:${Versions.lifecycleViewModel}"
    const val material = "com.google.android.material:material:${Versions.material}"
    const val materialPopupMenu = "com.github.zawadz88:MaterialPopupMenu:${Versions.materialPopupMenu}"
    const val okhttp = "com.squareup.okhttp3:okhttp:${Versions.okhttp}"
    const val recyclerView = "androidx.recyclerview:recyclerview:${Versions.recyclerView}"
    const val recyclerViewUtils = "com.github.Tunous:RecyclerViewUtils:${Versions.recyclerViewUtils}"
    const val sqlDelight = "com.squareup.sqldelight:android-driver:${Versions.sqlDelight}"
    const val timber = "com.jakewharton.timber:timber:${Versions.timber}"
    const val work = "androidx.work:work-runtime-ktx:${Versions.work}"
    const val workTesting = "androidx.work:work-testing:${Versions.work}"

    const val tools_gradleAndroid = "com.android.tools.build:gradle:${Versions.gradleAndroid}"
    const val tools_kotlin = "org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions.kotlin}"
    const val tools_sqlDelight = "com.squareup.sqldelight:gradle-plugin:${Versions.sqlDelight}"

    const val coreTesting = "androidx.arch.core:core-testing:${Versions.coreTesting}"
    const val espresso = "androidx.test.espresso:espresso-core:${Versions.espresso}"
    const val espressoContrib = "androidx.test.espresso:espresso-contrib:${Versions.espresso}"
    const val espressoIdlingResource = "androidx.test.espresso:espresso-idling-resource:${Versions.espresso}"
    const val espressoIntents = "androidx.test.espresso:espresso-intents:${Versions.espresso}"
    const val junit = "junit:junit:${Versions.junit}"
    const val testRules = "androidx.test:rules:${Versions.androidxTesting}"
    const val testRunner = "androidx.test:runner:${Versions.androidxTesting}"
}
