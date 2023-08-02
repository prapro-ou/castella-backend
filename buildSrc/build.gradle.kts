plugins {
    `kotlin-dsl`
}

dependencies {
    implementation(libs.gradle.kotlin)
}

gradlePlugin {
    plugins {
        register("kotlin") {
            id = "com.vb4.kotlin"
            implementationClass = "plugins.KotlinPlugin"
        }
        register("ktlint") {
            id = "com.vb4.ktlint"
            implementationClass = "plugins.KtlintPlugin"
        }
    }
}
