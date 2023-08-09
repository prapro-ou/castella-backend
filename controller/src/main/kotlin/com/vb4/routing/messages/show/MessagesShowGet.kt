package com.vb4.routing.messages.show

import com.vb4.GetMessageByIdUseCase
import com.vb4.result.consume
import com.vb4.result.flatMap
import com.vb4.result.mapBoth
import com.vb4.routing.ExceptionSerializable
import com.vb4.routing.getParameter
import io.ktor.server.application.call
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import com.vb4.message.Message
import com.vb4.message.MessageId
import com.vb4.message.Reply
import org.koin.ktor.ext.inject

fun Route.messagesShowGet() {
    val getMessageByIdUseCase by inject<GetMessageByIdUseCase>()

    get("{messageId}") {
        call.getParameter<String>("messageId")
            .flatMap { messageId -> getMessageByIdUseCase(MessageId(messageId)) }
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
        fun from(message: Message) = MessagesShowGetResponse(
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
        fun from(message: Message) = listOf(
            MessageSerializable(
                id = message.id.value,
                email = message.sender.email.value,
                body = message.body.value,
                createdAt = message.createdAt.value,
            ),
        ) + message.replies.map { reply -> from(reply = reply) }

        fun from(reply: Reply) = MessageSerializable(
            id = reply.id.value,
            email = reply.sender.email.value,
            body = reply.body.value,
            createdAt = reply.createdAt.value,
        )
    }
}
