package com.vb4.plugins

import com.vb4.plugins.auth.configureAuthenticationPlugin
import io.ktor.server.application.Application

fun Application.installPlugins() {
    configureAuthenticationPlugin()
    configureKoinPlugin()
    configureSerializerPlugin()
    configureSessionPlugin()
}
