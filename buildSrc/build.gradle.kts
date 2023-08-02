plugins {
    `kotlin-dsl`
}

dependencies {
    implementation(libs.gradle.kotlin)
}

gradlePlugin {
    plugins {
        register("ktlint") {
            id = "com.vb4.ktlint"
            implementationClass = "plugins.KtlintPlugin"
        }
    }
}
