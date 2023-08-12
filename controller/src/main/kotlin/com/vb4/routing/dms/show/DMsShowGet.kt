package com.vb4.routing.dms.show

import com.vb4.dm.GetDMMessagesByDMIdUseCase
import com.vb4.dm.DMId
import com.vb4.dm.DMMessage
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
import org.koin.ktor.ext.inject

fun Route.dmsShowGet() {
    val getDMMessagesByDMIdUseCase by inject<GetDMMessagesByDMIdUseCase>()

    get("{dmId}") {
        call.getParameter<String>("dmId")
            .flatMap { id -> getDMMessagesByDMIdUseCase(DMId(id)) }
            .mapBoth(
                success = { messages -> DMsShowGetResponse.from(messages) },
                failure = { ExceptionSerializable.from(it) },
            )
            .consume(
                success = { response -> call.respond(response) },
                failure = { (exception, status) -> call.respond(status, exception.message) },
            )
    }
}

@Serializable
private data class DMsShowGetResponse(
    val messages: List<DMMessageSerializable>,
) {
    companion object {
        fun from(messages: List<DMMessage>) = DMsShowGetResponse(
            messages = messages.map { DMMessageSerializable.from(it) },
        )
    }
}

@Serializable
private data class DMMessageSerializable(
    val id: String,
    val subject: String,
    val body: String,
    @SerialName("created_at") val createdAt: Instant,
    @SerialName("reply_count") val replyCount: Int,
) {
    companion object {
        fun from(message: DMMessage) = DMMessageSerializable(
            id = message.id.value,
            subject = message.subject.value,
            body = message.body.value,
            createdAt = message.createdAt.value,
            replyCount = message.replies.count(),
        )
    }
}
