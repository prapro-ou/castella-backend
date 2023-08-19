package com.vb4.plugins

import com.vb4.user.User
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.sessions.Sessions
import io.ktor.server.sessions.cookie

const val CASTELLA_SESSION = "castella_session"

fun Application.configureSessionPlugin() {
    install(Sessions) {
        cookie<CastellaSession>(CASTELLA_SESSION)
    }
}

data class CastellaSession(val authUser: User.AuthUser)
