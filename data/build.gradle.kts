plugins {
    id(Plugins.kotlin)
}

dependencies {
    implementation(project(":domain"))
    implementation(project(":utils"))

    implementation(libs.bundles.database)
    implementation(libs.java.mail)
}
