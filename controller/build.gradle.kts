plugins {
    id(Plugins.kotlin)
    id("io.ktor.plugin") version "2.3.2"
}

dependencies {
    implementation(project(":domain"))
    implementation(project(":usecase"))
    implementation(project(":data"))
    implementation(project(":utils"))

    implementation(libs.kotlin.serialization)
    implementation(libs.bundles.ktor)
    implementation(libs.bundles.koin)

    testImplementation(libs.bundles.test)
}
