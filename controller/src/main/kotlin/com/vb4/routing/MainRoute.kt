package com.vb4.routing

import com.vb4.routing.destinations.index.destinationsIndexGet
import com.vb4.routing.messages.show.messagesShowGet
import com.vb4.routing.dms.show.dmsShowGet
import io.ktor.server.application.Application
import io.ktor.server.routing.route
import io.ktor.server.routing.routing

fun Application.mainRoute() {
    routing {
        route("destinations") { destinationsIndexGet() }
        route("dms") { dmsShowGet() }
        route("groups") {}
        route("messages") { messagesShowGet() }
    }
}
