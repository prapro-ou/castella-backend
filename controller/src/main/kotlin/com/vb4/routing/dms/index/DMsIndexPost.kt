package com.vb4.routing.dms.index

import com.vb4.Email
import com.vb4.avatar.Avatar
import com.vb4.dm.CreateDMUseCase
import com.vb4.dm.DMName
import com.vb4.result.consume
import com.vb4.result.flatMap
import com.vb4.result.mapBoth
import com.vb4.routing.ExceptionSerializable
import com.vb4.routing.getRequest
import io.ktor.server.application.call
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.post
import kotlinx.serialization.Serializable
import org.koin.ktor.ext.inject

fun Route.dMsIndexPost() {
    val createDMUseCase by inject<CreateDMUseCase>()

    post("") {
        call.getRequest<DMsIndexPostRequest>()
            .flatMap { request ->
                createDMUseCase(
                    name = DMName(request.name),
                    userEmail = Email(""),
                    to = Avatar.reconstruct(Email(request.to))
                )
            }
            .mapBoth(
                success = { DMsIndexPostResponse(isSuccess = true) },
                failure = { ExceptionSerializable.from(it) },
            )
            .consume(
                success = { response -> call.respond(response) },
                failure = { (exception, status) -> call.respond(status, exception.message) },
            )
    }
}

@Serializable
data class DMsIndexPostRequest(
    val name: String,
    val to: String,
)

@Serializable
data class DMsIndexPostResponse(val isSuccess: Boolean)