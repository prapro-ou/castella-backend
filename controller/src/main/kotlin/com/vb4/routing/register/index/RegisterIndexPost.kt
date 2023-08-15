package com.vb4.routing.register.index

import com.vb4.Email
import com.vb4.result.consume
import com.vb4.result.flatMap
import com.vb4.result.mapBoth
import com.vb4.routing.ExceptionSerializable
import com.vb4.routing.getRequest
import com.vb4.user.LoginPassword
import com.vb4.user.MailPassword
import com.vb4.user.RegisterUserUseCase
import io.ktor.server.application.call
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.post
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.koin.ktor.ext.inject

fun Route.registerIndexPost() {
    val registerUserUseCase by inject<RegisterUserUseCase>()

    post {
        call.getRequest<RegisterIndexPostRequest>()
            .flatMap { (email, loginPassword, mailPassword) ->
                registerUserUseCase(
                    email = Email(email),
                    loginPassword = LoginPassword(loginPassword),
                    mailPassword = MailPassword(mailPassword),
                )
            }
            .mapBoth(
                success = { RegisterIndexPostResponse(isSuccess = true) },
                failure = { ExceptionSerializable.from(it) },
            )
            .consume(
                success = { response -> call.respond(response) },
                failure = { (exception, status) -> call.respond(status, exception.message) },
            )
    }
}

@Serializable
private data class RegisterIndexPostRequest(
    val email: String,
    @SerialName("login_password") val loginPassword: String,
    @SerialName("mail_password") val mailPassword: String,
)

@Serializable
private data class RegisterIndexPostResponse(
    @SerialName("is_success") val isSuccess: Boolean,
)
