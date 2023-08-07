package com.vb4.routing.destinations.show

import GetMessagesByDestinationUseCase
import com.vb4.routing.getParameter
import com.vb4.result.consume
import com.vb4.result.flatMap
import com.vb4.result.mapBoth
import com.vb4.routing.ExceptionSerializable
import destination.DestinationId
import io.ktor.server.application.call
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import message.CreatedAt
import message.Message
import org.koin.ktor.ext.inject

fun Route.destinationsShowGet() {
    val getMessagesByDestinationUseCase by inject<GetMessagesByDestinationUseCase>()

    get("{destinationId}") {
        call.getParameter<String>("destinationId")
            .flatMap { id -> getMessagesByDestinationUseCase(DestinationId(id)) }
            .mapBoth(
                success = { messages -> GetDestinationShowResponse.from(messages) },
                failure = { ExceptionSerializable.from(it) },
            )
            .consume(
                success = { response -> call.respond(response) },
                failure = { (exception, status) -> call.respond(status, exception.message) },
            )
    }
}

@Serializable
private data class GetDestinationShowResponse(
    val messages: List<MessageSerializable>,
) {
    companion object {
        fun from(messages: List<Message>) = GetDestinationShowResponse(
            messages = messages.map { MessageSerializable.from(it) }
        )
    }
}


@Serializable
private data class MessageSerializable(
    val id: String,
    val subject: String,
    val body: String,
    @SerialName("created_at") val createdAt: Instant,
    @SerialName("reply_count") val replyCount: Int,
) {
    companion object {
        fun from(message: Message) = MessageSerializable(
            id = message.id.value,
            subject = message.subject.value,
            body = message.body.value,
            createdAt = message.createdAt.value,
            replyCount = message.replies.count(),
        )
    }
}
