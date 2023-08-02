package com.vb4

import com.vb4.plugins.configureRouting
import com.vb4.plugins.installPlugins
import io.ktor.server.application.Application
import io.ktor.server.netty.EngineMain

fun main(args: Array<String>) = EngineMain.main(args)

fun Application.module() {
    installPlugins()
    configureRouting()
}
