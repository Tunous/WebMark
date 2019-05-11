@file:Suppress("unused", "SpellCheckingInspection")

import org.gradle.api.JavaVersion

object Config {
    const val minSdk = 21
    const val compileSdk = 28
    const val targetSdk = 28
    const val buildTools = "28.0.3"
    val javaVersion = JavaVersion.VERSION_1_8
}

object Deps {

    const val androidGradlePlugin = "com.android.tools.build:gradle:3.4.0"

    private const val appCompatVersion = "1.0.2"
    const val androidxAppCompat = "androidx.appcompat:appcompat:$appCompatVersion}"

    private const val lifecycleVersion = "2.0.0"
    const val lifecycle = "androidx.lifecycle:lifecycle-extensions:$lifecycleVersion"
    const val liveData = "androidx.lifecycle:lifecycle-livedata:$lifecycleVersion"
    const val lifecycleTesting = "androidx.arch.core:core-testing:$lifecycleVersion"

    private const val viewModelVersion = "2.1.0-alpha03"
    const val viewModel = "androidx.lifecycle:lifecycle-viewmodel-ktx:$viewModelVersion"

    private const val constraintLayoutVersion = "2.0.0-alpha3"
    const val constraintLayout =
        "androidx.constraintlayout:constraintlayout:$constraintLayoutVersion"

    private const val ktxVersion = "1.1.0-alpha05"
    const val ktx = "androidx.core:core-ktx:$ktxVersion"

    private const val recyclerViewVersion = "1.0.0"
    const val recyclerView = "androidx.recyclerview:recyclerview:$recyclerViewVersion"

    private const val workVersion = "2.1.0-alpha01"
    const val work = "androidx.work:work-runtime-ktx:$workVersion"
    const val workTesting = "androidx.work:work-testing:$workVersion"

    private const val testCoreVersion = "1.1.0"
    const val testCore = "androidx.test:core:$testCoreVersion"

    private const val androidxTestVersion = "1.1.1"
    const val testRunner = "androidx.test:runner:$androidxTestVersion"
    const val testRules = "androidx.test:rules:$androidxTestVersion"

    private const val espressoVersion = "3.1.1"
    const val espresso = "androidx.test.espresso:espresso-core:$espressoVersion"
    const val espressoContrib = "androidx.test.espresso:espresso-contrib:$espressoVersion"
    const val espressoIdlingResource =
        "androidx.test.espresso:espresso-idling-resource:$espressoVersion"
    const val espressoIntents = "androidx.test.espresso:espresso-intents:$espressoVersion"

    private const val junitVersion = "4.12"
    const val junit = "junit:junit:$junitVersion"

    private const val robolectricVersion = "4.2.1"
    const val robolectric = "org.robolectric:robolectric:$robolectricVersion"

    private const val glideVersion = "4.9.0"
    const val glide = "com.github.bumptech.glide:glide:$glideVersion"
    const val glideCompiler = "com.github.bumptech.glide:compiler:$glideVersion"

    const val material = "com.google.android.material:material:1.1.0-alpha05"

    const val materialPopupMenu =
        "com.github.zawadz88.materialpopupmenu:material-popup-menu:3.4.0"

    const val recyclerViewUtils =
        "com.github.Tunous:RecyclerViewUtils:c6e595ab0f"

    private const val coroutinesVersion = "1.2.1"
    const val coroutinesAndroid =
        "org.jetbrains.kotlinx:kotlinx-coroutines-android:$coroutinesVersion"
    const val coroutinesCore =
        "org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutinesVersion"

    const val crux = "com.github.chimbori:crux:2.1.0"

    const val jsoup = "org.jsoup:jsoup:1.11.3"

    private const val kodeinVersion = "6.2.0"
    const val kodein = "org.kodein.di:kodein-di-generic-jvm:$kodeinVersion"
    const val kodeinAndroid = "org.kodein.di:kodein-di-framework-android-x:$kodeinVersion"

    private const val kotlinVersion = "1.3.31"
    const val kotlin = "org.jetbrains.kotlin:kotlin-stdlib-jdk7:$kotlinVersion"
    const val kotlinGradlePlugin = "org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinVersion"

    const val kotpref = "com.chibatching.kotpref:kotpref:2.8.0"

    const val okhttp = "com.squareup.okhttp3:okhttp:3.14.1"

    const val timber = "com.jakewharton.timber:timber:4.7.1"

    private const val sqlDelightVersion = "1.1.3"
    const val sqlDelight = "com.squareup.sqldelight:android-driver:$sqlDelightVersion"
    const val sqlDelightGradlePlugin = "com.squareup.sqldelight:gradle-plugin:$sqlDelightVersion"
}
