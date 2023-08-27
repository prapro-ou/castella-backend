package com.vb4.routing.destinations.dms.show.messages.show

import com.vb4.datetime.toDisplayString
import com.vb4.dm.DMId
import com.vb4.dm.DMMessage
import com.vb4.dm.DMMessageId
import com.vb4.dm.DMReply
import com.vb4.dm.GetDMMessageByIdUseCase
import com.vb4.plugins.auth.authUser
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
import org.koin.core.parameter.parametersOf
import org.koin.ktor.ext.inject

fun Route.destinationsDMsShowMessagesShowGet() {
    get("{dmId}/{messageId}") {
        val getDmMessageByIdUseCase by this@destinationsDMsShowMessagesShowGet
            .inject<GetDMMessageByIdUseCase> { parametersOf(call.authUser) }

        call.getTwoParameter<String, String>("dmId", "messageId")
            .flatMap { (dmId, messageId) -> getDmMessageByIdUseCase(DMId(dmId), DMMessageId(messageId)) }
            .mapBoth(
                success = { messages -> DestinationsDMsMessagesShowGetResponse.from(messages) },
                failure = { ExceptionSerializable.from(it) },
            )
            .consume(
                success = { response -> call.respond(response) },
                failure = { (exception, status) -> call.respond(status, exception.message) },
            )
    }
}

@Serializable
private data class DestinationsDMsMessagesShowGetResponse(
    val messages: List<DMMessageSerializable>,
) {
    companion object {
        fun from(message: DMMessage) = DestinationsDMsMessagesShowGetResponse(
            messages = DMMessageSerializable.from(message),
        )
    }
}

@Serializable
private data class DMMessageSerializable(
    val id: String,
    val from: String,
    val body: String,
    @SerialName("is_recent") val isRecent: Boolean,
    @SerialName("created_at") val createdAt: String,
) {
    companion object {
        fun from(message: DMMessage) = listOf(
            DMMessageSerializable(
                id = message.id.value,
                from = message.from.email.value,
                body = message.body.value,
                isRecent = message.isRecent,
                createdAt = message.createdAt.value.toDisplayString(),
            ),
        ) + message.replies.map { reply -> from(reply = reply) }

        fun from(reply: DMReply) = DMMessageSerializable(
            id = reply.id.value,
            from = reply.from.email.value,
            body = reply.body.value,
            isRecent = reply.isRecent,
            createdAt = reply.createdAt.value.toDisplayString(),
        )
    }
}
