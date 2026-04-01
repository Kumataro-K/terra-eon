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

val natives by configurations.creating {
    isCanBeResolved = true
    isCanBeConsumed = false
    isTransitive = false
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

val copyNatives = tasks.register("copyNatives") {
    inputs.files(natives)
    val outputDir = file("build/libs/gdx-natives")
    outputs.dir(outputDir)

    doLast {
        outputDir.deleteRecursively()
        outputDir.mkdirs()

        natives.files.forEach { jar ->
            val abi = when {
                jar.name.contains("arm64-v8a") -> "arm64-v8a"
                jar.name.contains("armeabi-v7a") -> "armeabi-v7a"
                jar.name.contains("x86_64") -> "x86_64"
                jar.name.contains("x86") -> "x86"
                else -> null
            }

            if (abi != null) {
                copy {
                    from(zipTree(jar))
                    into(outputDir.resolve(abi))
                    include("*.so")
                }
            }
        }
    }
}

tasks.named("preBuild") {
    dependsOn(copyNatives)
}
