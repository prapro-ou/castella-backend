package com.vb4.plugins.auth

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.vb4.DomainException
import com.vb4.Email
import com.vb4.result.ApiResult
import com.vb4.result.mapBoth
import com.vb4.routing.ExceptionSerializable
import com.vb4.user.GetUserUseCase
import com.vb4.user.MailPassword
import com.vb4.user.User
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.auth.Authentication
import io.ktor.server.auth.Principal
import io.ktor.server.auth.jwt.jwt
import io.ktor.server.response.respond
import kotlinx.datetime.Clock
import kotlinx.datetime.toJavaInstant
import org.koin.ktor.ext.inject

const val JWT_AUTH = "jwt-auth"

fun Application.configureAuthenticationPlugin() {
    install(Authentication) {
        jwt(JWT_AUTH) {
            realm = castellaRealm
            verifier(
                JWT.require(Algorithm.HMAC256(secret))
                    .withAudience(audience)
                    .withIssuer(issuer)
                    .build(),
            )
            validate { credential ->
                val getUserUseCase by inject<GetUserUseCase>()

                credential.payload
                    .email
                    ?.let { email ->
                        val user = getUserUseCase(email)
                        if (user is ApiResult.Success) AuthUserPrincipal.from(user.value)
                        else null
                    }
            }
            challenge { _, _ ->
                DomainException.AuthException("Token is not valid or has expired")
                    .let { ExceptionSerializable.from(it) }
                    .also { (exception, status) -> call.respond(status, exception.message) }
            }
        }
    }
}

fun createJWT(email: Email): String = JWT.create()
    .withAudience(audience)
    .withIssuer(issuer)
    .withEmail(email)
    .withExpiresAt(Clock.System.now().toJavaInstant().plusSeconds(60000))
    .sign(Algorithm.HMAC256(secret))

data class AuthUserPrincipal(
    val email: Email,
    val password: MailPassword,
) : Principal {
    fun toDomain() = User.AuthUser.reconstruct(
        email = email,
        password = password,
    )

    companion object {
        fun from(user: User.AuthUser) = AuthUserPrincipal(
            email = user.email,
            password = user.password,
        )
    }
}
