package com.vb4.routing

import com.vb4.routing.destinations.index.destinationsIndexGet
import com.vb4.routing.destinations.show.messages.index.destinationsShowMessagesIndexGet
import com.vb4.routing.destinations.show.messages.show.destinationsShowMessagesShowGet
import io.ktor.server.application.Application
import io.ktor.server.routing.route
import io.ktor.server.routing.routing

fun Application.mainRoute() {
    val destinationId = "destinationId"
    val messageId = "messageId"

    routing {
        route("destinations") {
            destinationsIndexGet("")
            route("{$destinationId}") {
                destinationsShowMessagesIndexGet("", destinationId)
                destinationsShowMessagesShowGet("{$messageId}", messageId)
            }
        }
    }
}
