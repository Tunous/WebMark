
import java.io.FileInputStream
import java.util.Properties

plugins {
    id("com.android.application")
    id("kotlin-android")
    id("kotlin-android-extensions")
    id("kotlin-kapt")
    id("com.github.triplet.play")
}

android {
    buildToolsVersion = Config.buildTools
    compileSdkVersion(Config.compileSdk)

    defaultConfig {
        minSdkVersion(Config.minSdk)
        targetSdkVersion(Config.targetSdk)
        applicationId = "me.thanel.webmark"
        versionCode = 1
        versionName = "0.1.0"
        testInstrumentationRunner = "me.thanel.webmark.test.WebMarkRunner"
    }

    compileOptions {
        sourceCompatibility = Config.javaVersion
        targetCompatibility = Config.javaVersion
    }

    signingConfigs {
        register("release") {
            val keystorePropertiesFile = rootProject.file("signing/signing.properties")

            if (!keystorePropertiesFile.exists()) {
                logger.warn("Release builds may not work: signing config not found.")
                return@register
            }

            val keystoreProperties = Properties()
            keystoreProperties.load(FileInputStream(keystorePropertiesFile))

            storeFile = rootProject.file("signing/app-release.jks")
            keyAlias = keystoreProperties.getProperty("keyAlias")
            storePassword = keystoreProperties.getProperty("storePassword")
            keyPassword = keystoreProperties.getProperty("keyPassword")
        }
    }

    buildTypes {
        named("release") {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
            signingConfig = signingConfigs.getByName("release")
        }
        named("debug") {
            versionNameSuffix = "-debug"
            applicationIdSuffix = ".debug"
        }
    }

    packagingOptions {
        exclude("META-INF/library_release.kotlin_module")
    }

    testOptions {
        animationsDisabled = true
    }
}

androidExtensions {
    isExperimental = true
}

play {
    track = "internal"
    defaultToAppBundles = true
    serviceAccountCredentials = rootProject.file("signing/play-account.json")
}

dependencies {
    implementation(project(":libraries:database"))

    kapt(Deps.glideCompiler)

    implementation(Deps.androidxAppCompat)
    implementation(Deps.constraintLayout)
    implementation(Deps.coroutinesAndroid)
    implementation(Deps.coroutinesCore)
    implementation(Deps.crux)
    implementation(Deps.espressoIdlingResource)
    implementation(Deps.glide)
    implementation(Deps.jsoup)
    implementation(Deps.kodein)
    implementation(Deps.kodeinAndroid)
    implementation(Deps.kotlin)
    implementation(Deps.kotpref)
    implementation(Deps.ktx)
    implementation(Deps.lifecycle)
    implementation(Deps.material)
    implementation(Deps.materialPopupMenu)
    implementation(Deps.okhttp)
    implementation(Deps.recyclerView)
    implementation(Deps.recyclerViewUtils)
    implementation(Deps.timber)
    implementation(Deps.viewModel)
    implementation(Deps.work)

    testImplementation(Deps.junit)

    androidTestImplementation(Deps.lifecycleTesting)
    androidTestImplementation(Deps.espresso)
    androidTestImplementation(Deps.espressoContrib)
    androidTestImplementation(Deps.espressoIntents)
    androidTestImplementation(Deps.sqlDelight)
    androidTestImplementation(Deps.testRules)
    androidTestImplementation(Deps.testRunner)
    androidTestImplementation(Deps.workTesting)
}
