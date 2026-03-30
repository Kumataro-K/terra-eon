plugins {
    id("com.android.application")
    kotlin("android") version "1.9.22"
}

android {
    namespace = "com.lifesurvivor.android"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.lifesurvivor"
        minSdk = 21
        targetSdk = 34
        versionCode = 1
        versionName = "1.0.0"
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }

    sourceSets {
        getByName("main") {
            java.srcDirs("src/main/kotlin")
            manifest.srcFile("AndroidManifest.xml")
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
        }
    }
}

dependencies {
    val gdxVersion = "1.12.1"
    implementation(project(":core"))
    implementation("com.badlogicgames.gdx:gdx-backend-android:$gdxVersion")
    natives("com.badlogicgames.gdx:gdx-platform:$gdxVersion:natives-armeabi-v7a")
    natives("com.badlogicgames.gdx:gdx-platform:$gdxVersion:natives-arm64-v8a")
    natives("com.badlogicgames.gdx:gdx-platform:$gdxVersion:natives-x86")
    natives("com.badlogicgames.gdx:gdx-platform:$gdxVersion:natives-x86_64")
}

// ネイティブライブラリをコピーするヘルパー
fun DependencyHandler.natives(dependencyNotation: String) {
    add("implementation", dependencyNotation)
}
