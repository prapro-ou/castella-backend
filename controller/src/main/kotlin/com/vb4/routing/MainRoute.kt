package com.vb4.routing

import com.vb4.routing.destinations.index.destinationsIndexGet
import com.vb4.routing.dms.show.dmsShowGet
import com.vb4.routing.groups.show.groupsShowGet
import com.vb4.routing.dms.show.messages.show.dmsShowMessagesShowGet
import com.vb4.routing.groups.messages.show.groupsMessagesShowGet
import io.ktor.server.application.Application
import io.ktor.server.application.call
import io.ktor.server.response.respond
import io.ktor.server.routing.get
import io.ktor.server.routing.route
import io.ktor.server.routing.routing

fun Application.mainRoute() {
    routing {
        get("") { call.respond("Success") }
        route("destinations") { destinationsIndexGet() }
        route("dms") {
            dmsShowGet()
            dmsShowMessagesShowGet()
        }
        route("groups") {
            groupsShowGet()
            groupsMessagesShowGet()
        }
    }
}
