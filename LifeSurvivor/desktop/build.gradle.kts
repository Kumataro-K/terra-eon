plugins {
    kotlin("jvm") version "1.9.22"
    application
}

dependencies {
    val gdxVersion = "1.12.1"
    implementation(project(":core"))
    implementation("com.badlogicgames.gdx:gdx-backend-lwjgl3:$gdxVersion")
    implementation("com.badlogicgames.gdx:gdx-platform:$gdxVersion:natives-desktop")
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

kotlin {
    jvmToolchain(17)
}

application {
    mainClass.set("com.lifesurvivor.desktop.DesktopLauncherKt")
}

sourceSets {
    main {
        java.srcDirs("src/main/kotlin")
    }
}
