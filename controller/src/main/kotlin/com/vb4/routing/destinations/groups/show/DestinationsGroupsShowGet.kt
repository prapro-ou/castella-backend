package com.vb4.routing.destinations.groups.show

import com.vb4.datetime.toDisplayString
import com.vb4.group.GetGroupMessagesByGroupIdUseCase
import com.vb4.group.GroupId
import com.vb4.group.GroupMessage
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

fun Route.destinationsGroupsShowGet() {
    val getGroupMessagesByGroupIdUseCase by inject<GetGroupMessagesByGroupIdUseCase>()

    get("{groupId}") {
        call.getParameter<String>("groupId")
            .flatMap { id -> getGroupMessagesByGroupIdUseCase(GroupId(id)) }
            .mapBoth(
                success = { messages -> DestinationsGroupsShowGetResponse.from(messages) },
                failure = { ExceptionSerializable.from(it) },
            )
            .consume(
                success = { response -> call.respond(response) },
                failure = { (exception, status) -> call.respond(status, exception.message) },
            )
    }
}

@Serializable
private data class DestinationsGroupsShowGetResponse(
    val messages: List<GroupMessageSerializable>,
) {
    companion object {
        fun from(messages: List<GroupMessage>) = DestinationsGroupsShowGetResponse(
            messages = messages.map { GroupMessageSerializable.from(it) },
        )
    }
}

@Serializable
private data class GroupMessageSerializable(
    val id: String,
    val subject: String,
    val body: String,
    @SerialName("created_at") val createdAt: String,
    @SerialName("new_message_count") val newMessageCount: Int,
    @SerialName("reply_count") val replyCount: Int,
) {
    companion object {
        fun from(message: GroupMessage) = GroupMessageSerializable(
            id = message.id.value,
            subject = message.subject.value,
            body = message.body.value,
            createdAt = message.createdAt.value.toDisplayString(),
            newMessageCount = message.newMessageCount.value,
            replyCount = message.replies.count(),
        )
    }
}
