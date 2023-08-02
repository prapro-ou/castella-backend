plugins {
    id(Plugins.kotlin)
    alias(libs.plugins.ktor)
    alias(libs.plugins.serialization)
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
