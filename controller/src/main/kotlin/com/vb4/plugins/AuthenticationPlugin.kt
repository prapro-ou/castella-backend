package com.vb4.plugins

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.auth.Authentication
import io.ktor.server.auth.jwt.JWTPrincipal
import io.ktor.server.auth.jwt.jwt
import io.ktor.server.response.respond
import kotlinx.datetime.Clock
import kotlinx.datetime.toJavaInstant

private val secret = "secret"
private val audience = "audience"
private val issuer = "issuer"
private val castellaRealm = "Castella"

fun Application.configureAuthenticationPlugin() {
    install(Authentication) {
        jwt(JWT_AUTH) {
            realm = castellaRealm
            verifier(
                JWT.require(Algorithm.HMAC256(secret))
                    .withAudience(audience)
                    .withIssuer(issuer)
                    .build()
            )
            validate { JWTPrincipal(it.payload) }
            challenge { _, _ ->
                call.respond(HttpStatusCode.Unauthorized, "Token is not valid or has expired")
            }
        }
    }
}

fun createJWT(): String = JWT.create()
    .withAudience(audience)
    .withIssuer(issuer)
    .withExpiresAt(Clock.System.now().toJavaInstant().plusMillis(60000))
    .sign(Algorithm.HMAC256(secret))

const val JWT_AUTH = "jwt-auth"
