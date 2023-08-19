package com.vb4.plugins

import com.vb4.DomainException
import com.vb4.routing.ExceptionSerializable
import com.vb4.user.User
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.auth.Authentication
import io.ktor.server.auth.session
import io.ktor.server.response.respond
import io.ktor.server.sessions.Sessions
import io.ktor.server.sessions.cookie

const val CASTELLA_SESSION = "castella_session"
const val AUTH_SESSION = "auth-session"

fun Application.configureSessionPlugin() {
    install(Sessions) {
        cookie<AuthUserSession>(CASTELLA_SESSION)
    }
    install(Authentication) {
        session<AuthUserSession>(AUTH_SESSION) {
            challenge {
                DomainException.AuthException("Session is not valid or has expired")
                    .let { ExceptionSerializable.from(it) }
                    .also { (exception, status) -> call.respond(status, exception.message) }
            }
        }
    }
}

data class AuthUserSession(val authUser: User.AuthUser)
