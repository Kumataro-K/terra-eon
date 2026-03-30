plugins {
    kotlin("jvm") version "1.9.22"
}

dependencies {
    val gdxVersion = "1.12.1"
    api("com.badlogicgames.gdx:gdx:$gdxVersion")
    api("com.badlogicgames.gdx:gdx-backend-lwjgl3:$gdxVersion")
    api("com.badlogicgames.gdx:gdx-platform:$gdxVersion:natives-desktop")
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

kotlin {
    jvmToolchain(17)
}

sourceSets {
    main {
        java.srcDirs("src/main/kotlin")
    }
}
