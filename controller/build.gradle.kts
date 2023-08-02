plugins {
    kotlin("jvm") version "1.9.0"
    id("io.ktor.plugin") version "2.3.2"
}

dependencies {
    implementation(libs.bundles.ktor)
    testImplementation(libs.bundles.test)
}
