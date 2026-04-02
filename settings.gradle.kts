pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.PREFER_SETTINGS)
    repositories {
        google()
        mavenCentral()
        maven("https://oss.sonatype.org/content/repositories/snapshots/")
    }
}

rootProject.name = "terra-eon"

include(":core", ":android", ":desktop")
project(":core").projectDir = file("LifeSurvivor/core")
project(":android").projectDir = file("LifeSurvivor/android")
project(":desktop").projectDir = file("LifeSurvivor/desktop")
