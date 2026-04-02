// Life Survivor: 生命38億年の戦い
// ルートビルドファイル

plugins {
    kotlin("jvm") version "2.0.21" apply false
}

allprojects {
    repositories {
        google()
        mavenCentral()
        maven("https://oss.sonatype.org/content/repositories/snapshots/")
    }
    version = "1.0.0"
    group = "com.lifesurvivor"
}
