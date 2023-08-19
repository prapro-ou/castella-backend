package com.vb4.routing.login.index

import com.vb4.Email
import com.vb4.plugins.AuthUserSession
import com.vb4.result.consume
import com.vb4.result.flatMap
import com.vb4.result.mapBoth
import com.vb4.routing.ExceptionSerializable
import com.vb4.routing.getRequest
import com.vb4.user.AuthUserUseCase
import com.vb4.user.LoginPassword
import com.vb4.user.User
import io.ktor.server.application.call
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.post
import io.ktor.server.sessions.sessions
import io.ktor.server.sessions.set
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.koin.ktor.ext.inject

fun Route.loginIndexPost() {
    val authUserUseCase by inject<AuthUserUseCase>()

    post {
        call.getRequest<LoginIndexPostRequest>()
            .flatMap { (email, password) ->
                authUserUseCase(
                    user = User.BeforeAuthUser.reconstruct(
                        email = Email(email),
                        password = LoginPassword(password),
                    ),
                )
            }
            .mapBoth(
                success = { user ->
                    call.sessions.set(AuthUserSession(user))
                    LoginIndexPostResponse(isSuccess = true)
                },
                failure = { ExceptionSerializable.from(it) },
            )
            .consume(
                success = { response -> call.respond(response) },
                failure = { (exception, status) -> call.respond(status, exception.message) },
            )
    }
}

@Serializable
private data class LoginIndexPostRequest(
    val email: String,
    val password: String,
)

@Serializable
private data class LoginIndexPostResponse(
    @SerialName("is_success") val isSuccess: Boolean,
)
