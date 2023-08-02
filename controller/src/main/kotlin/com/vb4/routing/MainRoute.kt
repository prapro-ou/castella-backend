package com.vb4.routing

import com.vb4.routing.destinations.index.getDestinationIndex
import io.ktor.server.application.Application
import io.ktor.server.routing.route
import io.ktor.server.routing.routing

fun Application.mainRoute() {
    routing {
        route("destinations") {
            getDestinationIndex("")
        }
    }
}
