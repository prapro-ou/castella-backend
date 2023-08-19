package com.vb4.plugins.auth

import com.vb4.DomainException
import com.vb4.plugins.CastellaSession
import com.vb4.routing.ExceptionSerializable
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.auth.Authentication
import io.ktor.server.auth.session
import io.ktor.server.response.respond

const val AUTH_SESSION = "auth-session"

fun Application.configureSessionAuthPlugin() {
    install(Authentication) {
        session<CastellaSession>(AUTH_SESSION) {
            challenge {
                DomainException.AuthException("Token is not valid or has expired")
                    .let { ExceptionSerializable.from(it) }
                    .also { (exception, status) -> call.respond(status, exception.message) }
            }
        }
    }
}
