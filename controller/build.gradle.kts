plugins {
    id("io.ktor.plugin") version "2.3.2"
}

group = "com.vb4"
version = "0.0.1"
application {
    mainClass.set("com.vb4.ApplicationKt")

    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

dependencies {
    implementation(libs.bundles.ktor)
    testImplementation(libs.bundles.test)
}
