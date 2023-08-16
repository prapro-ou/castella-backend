package com.vb4.routing.groups.show

import com.vb4.group.CreateGroupMessageUseCase
import com.vb4.group.GroupBody
import com.vb4.group.GroupId
import com.vb4.group.GroupSubject
import com.vb4.plugins.auth.authUser
import com.vb4.result.consume
import com.vb4.result.flatMap
import com.vb4.result.map
import com.vb4.result.mapBoth
import com.vb4.routing.ExceptionSerializable
import com.vb4.routing.getParameter
import com.vb4.routing.getRequest
import io.ktor.server.application.call
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.post
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.koin.core.parameter.parametersOf
import org.koin.ktor.ext.inject

fun Route.groupsShowPost() {
    post("{groupId}") {
        val createGroupMessageUseCase by this@groupsShowPost
            .inject<CreateGroupMessageUseCase> { parametersOf(call.authUser) }

        call.getParameter<String>("groupId")
            .flatMap { groupId ->
                call.getRequest<GroupsShowPostRequest>()
                    .map { (subject, body) ->
                        createGroupMessageUseCase(
                            groupId = GroupId(groupId),
                            subject = GroupSubject(subject),
                            body = GroupBody(body),
                        )
                    }
            }
            .mapBoth(
                success = { GroupsShowPostResponse(isSuccess = true) },
                failure = { ExceptionSerializable.from(it) },
            )
            .consume(
                success = { response -> call.respond(response) },
                failure = { (exception, status) -> call.respond(status, exception.message) },
            )
    }
}


@Serializable
private data class GroupsShowPostRequest(val subject: String, val body: String)

@Serializable
private data class GroupsShowPostResponse(@SerialName("is_success") val isSuccess: Boolean)
