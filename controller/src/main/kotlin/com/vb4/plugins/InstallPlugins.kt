package com.vb4.plugins

import com.vb4.plugins.auth.configureJWTAuthPlugin
import io.ktor.server.application.Application

fun Application.installPlugins() {
    configureJWTAuthPlugin()
    configureKoinPlugin()
    configureSerializerPlugin()
}
