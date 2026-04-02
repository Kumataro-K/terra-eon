plugins {
    `java-library`
    kotlin("jvm")
}

dependencies {
    val gdxVersion = "1.12.1"
    api("com.badlogicgames.gdx:gdx:$gdxVersion")
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

kotlin {
    jvmToolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
    compilerOptions {
        jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_17)
    }
}

sourceSets {
    main {
        java.srcDirs("src/main/kotlin")
    }
}
