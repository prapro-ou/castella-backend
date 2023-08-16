package com.vb4.routing.groups.show.messages.show

import com.vb4.group.CreateGroupMessageUseCase
import com.vb4.group.CreateGroupReplyUseCase
import com.vb4.group.GroupBody
import com.vb4.group.GroupId
import com.vb4.group.GroupMessageId
import com.vb4.plugins.auth.authUser
import com.vb4.result.consume
import com.vb4.result.flatMap
import com.vb4.result.map
import com.vb4.result.mapBoth
import com.vb4.routing.ExceptionSerializable
import com.vb4.routing.getRequest
import com.vb4.routing.getTwoParameter
import io.ktor.server.application.call
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.post
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.koin.core.parameter.parametersOf
import org.koin.ktor.ext.inject

fun Route.groupsShowMessagesShowPost() {
    post("{groupId}/{messageId}") {
        val createGroupReplyUseCase by this@groupsShowMessagesShowPost
            .inject<CreateGroupReplyUseCase> { parametersOf(call.authUser) }

        call.getTwoParameter<String, String>("groupId", "messageId")
            .flatMap { (groupId, messageId) ->
                call.getRequest<GroupsShowMessagesShowPostRequest>()
                    .map { (body) ->
                        createGroupReplyUseCase(
                            groupId = GroupId(groupId),
                            groupMessageId = GroupMessageId(messageId),
                            body = GroupBody(body),
                        )
                    }
            }
            .mapBoth(
                success = { GroupsShowMessagesShowPostResponse(isSuccess = true) },
                failure = { ExceptionSerializable.from(it) },
            )
            .consume(
                success = { response -> call.respond(response) },
                failure = { (exception, status) -> call.respond(status, exception.message) },
            )
    }
}

@Serializable
private data class GroupsShowMessagesShowPostRequest(
    val body: String,
)

@Serializable
private data class GroupsShowMessagesShowPostResponse(@SerialName("is_success") val isSuccess: Boolean)
