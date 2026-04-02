pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

rootProject.name = "terra-eon"

include(":core", ":desktop")
project(":core").projectDir = file("LifeSurvivor/core")
project(":desktop").projectDir = file("LifeSurvivor/desktop")

// Androidモジュールは Android SDK が存在する場合のみ含める
val androidSdkDir = System.getenv("ANDROID_HOME") ?: System.getenv("ANDROID_SDK_ROOT")
    ?: file("local.properties").takeIf { it.exists() }?.let {
        java.util.Properties().apply { load(it.inputStream()) }.getProperty("sdk.dir")
    }

if (androidSdkDir != null && file(androidSdkDir).exists()) {
    include(":android")
    project(":android").projectDir = file("LifeSurvivor/android")
}
