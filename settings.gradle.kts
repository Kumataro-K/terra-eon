rootProject.name = "terra-eon"

include(":core", ":android", ":desktop")
project(":core").projectDir = file("LifeSurvivor/core")
project(":android").projectDir = file("LifeSurvivor/android")
project(":desktop").projectDir = file("LifeSurvivor/desktop")
