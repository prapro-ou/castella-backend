package plugins

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.attributes.Bundling
import org.gradle.api.tasks.JavaExec
import org.gradle.kotlin.dsl.creating
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getValue
import org.gradle.kotlin.dsl.named
import org.gradle.kotlin.dsl.register

class KtlintPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            val ktlint by configurations.creating
            dependencies {
                // Version Catalogに登録不可
                ktlint("com.pinterest:ktlint:0.49.1") {
                    attributes {
                        attribute(Bundling.BUNDLING_ATTRIBUTE, objects.named(Bundling.EXTERNAL))
                    }
                }
            }

            tasks.register<JavaExec>("ktlintCheck") {
                group = "ktlint"
                description = "Check Kotlin code style."
                classpath = ktlint
                mainClass.set("com.pinterest.ktlint.Main")
                args = listOf("src/**/*.kt", "**.kts", "!**/build/**")
            }

            tasks.register<JavaExec>("ktlintFormat") {
                group = "ktlint"
                description = "Fix Kotlin code style deviations."
                classpath = ktlint
                mainClass.set("com.pinterest.ktlint.Main")
                jvmArgs = listOf("--add-opens=java.base/java.lang=ALL-UNNAMED")
                args = listOf("-F", "src/**/*.kt", "**.kts", "!**/build/**")
            }
        }
    }
}
