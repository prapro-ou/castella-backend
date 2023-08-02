plugins {
    `kotlin-dsl`
}

gradlePlugin {
    plugins {
        register("ktlint") {
            id = "com.vb4.ktlint"
            implementationClass = "plugins.KtlintPlugin"
        }
    }
}
