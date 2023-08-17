package com.vb4.routing.destinations.groups.index

import com.vb4.Email
import com.vb4.avatar.Avatar
import com.vb4.group.CreateGroupUseCase
import com.vb4.group.GroupName
import com.vb4.plugins.auth.authUser
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

fun Route.destinationsGroupsIndexPost() {
    post {
        val createGroupUseCase by this@destinationsGroupsIndexPost
            .inject<CreateGroupUseCase> { parametersOf(call.authUser) }

        call.getRequest<DestinationsGroupsIndexPostRequest>()
            .flatMap { (name, to) ->
                createGroupUseCase(
                    name = GroupName(name),
                    userEmail = call.authUser.email,
                    to = to.map { Avatar.reconstruct(Email(it)) },
                )
            }
            .mapBoth(
                success = { DestinationsGroupsIndexPostResponse(isSuccess = true) },
                failure = { ExceptionSerializable.from(it) },
            )
            .consume(
                success = { response -> call.respond(response) },
                failure = { (exception, status) -> call.respond(status, exception.message) },
            )
    }
}

@Serializable
private data class DestinationsGroupsIndexPostRequest(
    val name: String,
    val to: List<String>,
)

@Serializable
private data class DestinationsGroupsIndexPostResponse(@SerialName("is_success") val isSuccess: Boolean)
