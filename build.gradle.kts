import com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask

buildscript {
    repositories {
        google()
        jcenter()
        
    }
    dependencies {
        classpath(Deps.androidGradlePlugin)
        classpath(Deps.kotlinGradlePlugin)
        classpath(Deps.sqlDelightGradlePlugin)
    }
}

plugins {
    id("com.diffplug.gradle.spotless") version "3.23.0"
    id("com.github.ben-manes.versions") version "0.21.0"
    id("com.github.triplet.play") version "2.2.1"
}

allprojects {
    repositories {
        google()
        maven("https://jitpack.io")
        jcenter()
    }
}

subprojects {
    apply(plugin = "com.diffplug.gradle.spotless")
    spotless {
        kotlin {
            target("**/*.kt")
            ktlint("0.32.0")
        }
    }
}

// Disallow release candidate versions in dependency updates output
tasks.named<DependencyUpdatesTask>("dependencyUpdates") {
    resolutionStrategy {
        componentSelection {
            all {
                val rejected = listOf("alpha", "beta", "rc", "cr", "m", "preview", "b", "ea").any { qualifier ->
                    candidate.version.matches(Regex("(?i).*[.-]$qualifier[.\\d-+]*"))
                }
                if (rejected) {
                    reject("Release candidate")
                }
            }
        }
    }
}
