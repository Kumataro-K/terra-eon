plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
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
            jniLibs.srcDirs(file("build/libs/gdx-natives"))
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
        }
    }
}

val natives by configurations.creating

dependencies {
    val gdxVersion = "1.12.1"
    implementation(project(":core"))
    implementation("com.badlogicgames.gdx:gdx-backend-android:$gdxVersion")

    natives("com.badlogicgames.gdx:gdx-platform:$gdxVersion:natives-armeabi-v7a")
    natives("com.badlogicgames.gdx:gdx-platform:$gdxVersion:natives-arm64-v8a")
    natives("com.badlogicgames.gdx:gdx-platform:$gdxVersion:natives-x86")
    natives("com.badlogicgames.gdx:gdx-platform:$gdxVersion:natives-x86_64")
}

// ネイティブライブラリを jniLibs にコピーするタスク
tasks.register("copyNatives") {
    doLast {
        val abiMap = mapOf(
            "natives-armeabi-v7a" to "armeabi-v7a",
            "natives-arm64-v8a" to "arm64-v8a",
            "natives-x86" to "x86",
            "natives-x86_64" to "x86_64"
        )
        natives.files.forEach { file ->
            abiMap.forEach { (classifier, abi) ->
                if (file.name.contains(classifier)) {
                    val targetDir = layout.projectDirectory.dir("src/main/jniLibs/$abi").asFile
                    targetDir.mkdirs()
                    copy {
                        from(zipTree(file))
                        into(targetDir)
                        include("*.so")
                    }
                }
            }
        }
    }
}

tasks.matching { it.name.contains("merge") && it.name.contains("JniLibFolders") }.configureEach {
    dependsOn("copyNatives")
}
