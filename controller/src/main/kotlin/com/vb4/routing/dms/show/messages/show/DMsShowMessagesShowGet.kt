package com.vb4.routing.dms.show.messages.show

import com.vb4.dm.GetDMMessageByIdUseCase
import com.vb4.dm.DMId
import com.vb4.dm.DMMessage
import com.vb4.dm.DMMessageId
import com.vb4.dm.DMReply
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

fun Route.dmsShowMessagesShowGet() {
    val getMessageByIdUseCase by inject<GetDMMessageByIdUseCase>()

    get("{dmId}/{messageId}") {
        call.getTwoParameter<String, String>("dmId", "messageId")
            .flatMap { (dmId, messageId) -> getMessageByIdUseCase(DMId(dmId), DMMessageId(messageId)) }
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
        fun from(message: DMMessage) = MessagesShowGetResponse(
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
        fun from(message: DMMessage) = listOf(
            MessageSerializable(
                id = message.id.value,
                email = message.from.email.value,
                body = message.body.value,
                createdAt = message.createdAt.value,
            ),
        ) + message.replies.map { reply -> from(reply = reply) }

        fun from(reply: DMReply) = MessageSerializable(
            id = reply.id.value,
            email = reply.from.email.value,
            body = reply.body.value,
            createdAt = reply.createdAt.value,
        )
    }
}
