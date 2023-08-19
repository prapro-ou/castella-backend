package com.vb4.routing.destinations.dms.index

import com.vb4.Email
import com.vb4.avatar.Avatar
import com.vb4.dm.CreateDMUseCase
import com.vb4.dm.DMName
import com.vb4.plugins.authUser
import com.vb4.result.consume
import com.vb4.result.flatMap
import com.vb4.result.mapBoth
import com.vb4.routing.ExceptionSerializable
import com.vb4.routing.getRequest
import io.ktor.server.application.call
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.post
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.koin.core.parameter.parametersOf
import org.koin.ktor.ext.inject

fun Route.destinationsDMsIndexPost() {
    post {
        val createDMUseCase by this@destinationsDMsIndexPost
            .inject<CreateDMUseCase> { parametersOf(call.authUser) }

        call.getRequest<DestinationsDMsIndexPostRequest>()
            .flatMap { (name, to) ->
                createDMUseCase(
                    name = DMName(name),
                    userEmail = call.authUser.email,
                    to = Avatar.reconstruct(Email(to)),
                )
            }
            .mapBoth(
                success = { DestinationsDMsIndexPostResponse(isSuccess = true) },
                failure = { ExceptionSerializable.from(it) },
            )
            .consume(
                success = { response -> call.respond(response) },
                failure = { (exception, status) -> call.respond(status, exception.message) },
            )
    }
}

@Serializable
private data class DestinationsDMsIndexPostRequest(
    val name: String,
    val to: String,
)

@Serializable
private data class DestinationsDMsIndexPostResponse(@SerialName("is_success") val isSuccess: Boolean)
