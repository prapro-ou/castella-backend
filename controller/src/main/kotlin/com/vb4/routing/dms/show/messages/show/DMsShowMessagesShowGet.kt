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
    val getDmMessageByIdUseCase by inject<GetDMMessageByIdUseCase>()

    get("{dmId}/{messageId}") {
        call.getTwoParameter<String, String>("dmId", "messageId")
            .flatMap { (dmId, messageId) -> getDmMessageByIdUseCase(DMId(dmId), DMMessageId(messageId)) }
            .mapBoth(
                success = { messages -> DMsMessagesShowGetResponse.from(messages) },
                failure = { ExceptionSerializable.from(it) },
            )
            .consume(
                success = { response -> call.respond(response) },
                failure = { (exception, status) -> call.respond(status, exception.message) },
            )
    }
}

@Serializable
private data class DMsMessagesShowGetResponse(
    val messages: List<DMMessageSerializable>,
) {
    companion object {
        fun from(message: DMMessage) = DMsMessagesShowGetResponse(
            messages = DMMessageSerializable.from(message),
        )
    }
}

@Serializable
private data class DMMessageSerializable(
    val id: String,
    val email: String,
    val body: String,
    @SerialName("created_at") val createdAt: Instant,
) {
    companion object {
        fun from(message: DMMessage) = listOf(
            DMMessageSerializable(
                id = message.id.value,
                email = message.from.email.value,
                body = message.body.value,
                createdAt = message.createdAt.value,
            ),
        ) + message.replies.map { reply -> from(reply = reply) }

        fun from(reply: DMReply) = DMMessageSerializable(
            id = reply.id.value,
            email = reply.from.email.value,
            body = reply.body.value,
            createdAt = reply.createdAt.value,
        )
    }
}
