plugins {
    kotlin("multiplatform") version "1.9.0"
}

group = "ru.landgrafhomyak.utility"
version = "1.0"

repositories {
    mavenCentral()
}

repositories {
    mavenCentral()
}

kotlin {
    jvm {
        jvmToolchain(8)
    }
    js {
        browser()
        nodejs()
    }

    macosArm64()
    macosX64()
    linuxArm64()
    linuxX64()
    mingwX64()
    androidNativeArm32()
    androidNativeArm64()
    androidNativeX64()
    androidNativeX86()
    iosArm64()
    iosSimulatorArm64()
    iosX64()
    ios()
//    wasm()
    tvos()
    watchos()

    sourceSets {
        val commonMain by getting
    }
}