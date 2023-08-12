package com.vb4.routing.groups.messages.show

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

fun Route.groupsMessagesShowGet() {
    val getMessageByIdUseCase by inject<GetGroupMessageByIdUseCase>()

    get("{groupId}/{messageId}") {
        call.getTwoParameter<String, String>("groupId", "messageId")
            .flatMap { (groupId, messageId) -> getMessageByIdUseCase(GroupId(groupId), GroupMessageId(messageId)) }
            .mapBoth(
                success = { messages -> MessagesShowGetResponse.from(messages) },
                failure = { ExceptionSerializable.from(it) },
            )
            .consume(
                success = { response -> call.respond(response) },
                failure = { (exception, status) -> call.respond(status, exception.message) },
            )
    }
}

@Serializable
private data class MessagesShowGetResponse(
    val messages: List<MessageSerializable>,
) {
    companion object {
        fun from(message: GroupMessage) = MessagesShowGetResponse(
            messages = MessageSerializable.from(message),
        )
    }
}

@Serializable
private data class MessageSerializable(
    val id: String,
    val email: String,
    val body: String,
    @SerialName("created_at") val createdAt: Instant,
) {
    companion object {
        fun from(message: GroupMessage) = listOf(
            MessageSerializable(
                id = message.id.value,
                email = message.from.email.value,
                body = message.body.value,
                createdAt = message.createdAt.value,
            ),
        ) + message.replies.map { reply -> from(reply = reply) }

        fun from(reply: GroupReply) = MessageSerializable(
            id = reply.id.value,
            email = reply.from.email.value,
            body = reply.body.value,
            createdAt = reply.createdAt.value,
        )
    }
}