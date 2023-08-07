package com.vb4.routing

import com.vb4.routing.destinations.index.destinationsIndexGet
import com.vb4.routing.destinations.show.destinationsShowGet
import com.vb4.routing.messages.show.messagesShowGet
import io.ktor.server.application.Application
import io.ktor.server.routing.route
import io.ktor.server.routing.routing

fun Application.mainRoute() {
    routing {
        route("destinations") {
            destinationsIndexGet()
            destinationsShowGet()
        }
        route("messages") {
            messagesShowGet()
        }
    }
}
