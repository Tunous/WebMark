plugins {
    id("com.android.library")
    id("kotlin-android")
    id("com.squareup.sqldelight")
}

android {
    buildToolsVersion = Config.buildTools
    compileSdkVersion(Config.compileSdk)

    defaultConfig {
        minSdkVersion(Config.minSdk)
        targetSdkVersion(Config.targetSdk)
    }

    compileOptions {
        sourceCompatibility = Config.javaVersion
        targetCompatibility = Config.javaVersion
    }

    testOptions {
        unitTests.isIncludeAndroidResources = true
    }
}

dependencies {
    implementation(Deps.coroutinesAndroid)
    implementation(Deps.coroutinesCore)
    implementation(Deps.kodein)
    implementation(Deps.kodeinAndroid)
    implementation(Deps.liveData)
    implementation(Deps.sqlDelight)

    testImplementation(Deps.junit)
    testImplementation(Deps.robolectric)
    testImplementation(Deps.testCore)
}
