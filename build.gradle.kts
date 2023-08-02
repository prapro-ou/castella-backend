task<Delete>("clean") {
    delete(rootProject.buildDir)
}

subprojects {
    apply(plugin = Plugins.ktlint)
}
