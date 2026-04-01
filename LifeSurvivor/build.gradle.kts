// Life Survivor: 生命38億年の戦い
// ルートビルドファイル

buildscript {
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:8.9.0")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.9.22")
    }
}

allprojects {
    version = "1.0.0"
    group = "com.lifesurvivor"
}
