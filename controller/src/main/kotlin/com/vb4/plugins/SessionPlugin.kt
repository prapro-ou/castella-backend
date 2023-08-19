package com.vb4.plugins

import com.vb4.DomainException
import com.vb4.routing.ExceptionSerializable
import com.vb4.user.User
import io.ktor.server.application.Application
import io.ktor.server.application.ApplicationCall
import io.ktor.server.application.install
import io.ktor.server.auth.Authentication
import io.ktor.server.auth.Principal
import io.ktor.server.auth.principal
import io.ktor.server.auth.session
import io.ktor.server.response.respond
import io.ktor.server.sessions.SessionStorageMemory
import io.ktor.server.sessions.Sessions
import io.ktor.server.sessions.cookie
import io.ktor.util.hex

const val CASTELLA_SESSION = "castella-session"
const val AUTH_SESSION = "auth-session"

fun Application.configureSessionPlugin() {
    install(Sessions) {
        cookie<AuthUserSession>(CASTELLA_SESSION, SessionStorageMemory()) {
            cookie.path = "/"
            cookie.maxAgeInSeconds = 60
        }
    }
    install(Authentication) {
        session<AuthUserSession>(AUTH_SESSION) {
            validate { it }
            challenge {
                DomainException.AuthException("Session is not valid or has expired")
                    .let { ExceptionSerializable.from(it) }
                    .also { (exception, status) -> call.respond(status, exception.message) }
            }
        }
    }
}

data class AuthUserSession(val authUser: User.AuthUser) : Principal
val ApplicationCall.authUser get() = principal<AuthUserSession>()?.authUser
    ?: throw DomainException.AuthException("No valid session found.")
