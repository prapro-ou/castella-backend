package com.vb4.plugins

import com.vb4.plugins.auth.configureSessionAuthPlugin
import io.ktor.server.application.Application

fun Application.installPlugins() {
    configureKoinPlugin()
    configureSerializerPlugin()
    configureSessionPlugin()
    configureSessionAuthPlugin()
}
