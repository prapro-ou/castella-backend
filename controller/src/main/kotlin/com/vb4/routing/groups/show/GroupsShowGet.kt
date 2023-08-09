package com.vb4.routing.groups.show

import com.vb4.GetMessagesByGroupIdUseCase
import com.vb4.result.consume
import com.vb4.result.flatMap
import com.vb4.result.mapBoth
import com.vb4.routing.ExceptionSerializable
import com.vb4.routing.getParameter
import com.vb4.group.GroupId
import io.ktor.server.application.call
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import com.vb4.message.Message
import org.koin.ktor.ext.inject

fun Route.groupsShowGet() {
    val getMessagesByGroupIdUseCase by inject<GetMessagesByGroupIdUseCase>()

    get("{groupId}") {
        call.getParameter<String>("groupId")
            .flatMap { id -> getMessagesByGroupIdUseCase(GroupId(id)) }
            .mapBoth(
                success = { messages -> GetGroupsShowResponse.from(messages) },
                failure = { ExceptionSerializable.from(it) },
            )
            .consume(
                success = { response -> call.respond(response) },
                failure = { (exception, status) -> call.respond(status, exception.message) },
            )
    }
}

@Serializable
private data class GetGroupsShowResponse(
    val messages: List<MessageSerializable>,
) {
    companion object {
        fun from(messages: List<Message>) = GetGroupsShowResponse(
            messages = messages.map { MessageSerializable.from(it) },
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
