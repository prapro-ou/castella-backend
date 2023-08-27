package com.vb4.routing.destinations.groups.show.messages.show

import com.vb4.datetime.toDisplayString
import com.vb4.group.GetGroupMessageByIdUseCase
import com.vb4.group.GroupId
import com.vb4.group.GroupMessage
import com.vb4.group.GroupMessageId
import com.vb4.group.GroupReply
import com.vb4.result.consume
import com.vb4.result.flatMap
import com.vb4.result.mapBoth
import com.vb4.routing.ExceptionSerializable
import com.vb4.routing.getTwoParameter
import io.ktor.server.application.call
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.koin.ktor.ext.inject

fun Route.destinationsGroupsShowMessagesShowGet() {
    val getGroupMessageByIdUseCase by inject<GetGroupMessageByIdUseCase>()

    get("{groupId}/{messageId}") {
        call.getTwoParameter<String, String>("groupId", "messageId")
            .flatMap { (groupId, messageId) -> getGroupMessageByIdUseCase(GroupId(groupId), GroupMessageId(messageId)) }
            .mapBoth(
                success = { messages -> DestinationsGroupsMessagesShowGetResponse.from(messages) },
                failure = { ExceptionSerializable.from(it) },
            )
            .consume(
                success = { response -> call.respond(response) },
                failure = { (exception, status) -> call.respond(status, exception.message) },
            )
    }
}

@Serializable
private data class DestinationsGroupsMessagesShowGetResponse(
    val messages: List<GroupMessageSerializable>,
) {
    companion object {
        fun from(message: GroupMessage) = DestinationsGroupsMessagesShowGetResponse(
            messages = GroupMessageSerializable.from(message),
        )
    }
}

@Serializable
private data class GroupMessageSerializable(
    val id: String,
    val from: String,
    val body: String,
    @SerialName("is_recent") val isRecent: Boolean,
    @SerialName("created_at") val createdAt: String,
) {
    companion object {
        fun from(message: GroupMessage) = listOf(
            GroupMessageSerializable(
                id = message.id.value,
                from = message.from.email.value,
                body = message.body.value,
                isRecent = message.isRecent,
                createdAt = message.createdAt.value.toDisplayString(),
            ),
        ) + message.replies.map { reply -> from(reply = reply) }

        fun from(reply: GroupReply) = GroupMessageSerializable(
            id = reply.id.value,
            from = reply.from.email.value,
            body = reply.body.value,
            isRecent = reply.isRecent,
            createdAt = reply.createdAt.value.toDisplayString(),
        )
    }
}
