package com.vb4.plugins

import io.ktor.server.application.Application

fun Application.installPlugins() {
    configureKoinPlugin()
}
