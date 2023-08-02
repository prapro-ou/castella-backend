plugins {
    id(Plugins.kotlin)
    id("io.ktor.plugin") version "2.3.2"
}

dependencies {
    implementation(libs.bundles.ktor)
    testImplementation(libs.bundles.test)
}
