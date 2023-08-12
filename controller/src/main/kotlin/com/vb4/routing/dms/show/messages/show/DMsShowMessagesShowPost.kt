package com.vb4.routing.dms.show.messages.show

import com.vb4.dm.CreateDMReplyUseCase
import com.vb4.dm.DMBody
import com.vb4.dm.DMId
import com.vb4.dm.DMMessageId
import com.vb4.result.consume
import com.vb4.result.flatMap
import com.vb4.result.map
import com.vb4.result.mapBoth
import com.vb4.routing.ExceptionSerializable
import com.vb4.routing.getRequest
import com.vb4.routing.getTwoParameter
import io.ktor.server.application.call
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.post
import kotlinx.serialization.Serializable
import org.koin.ktor.ext.inject

fun Route.dMsShowMessagesShowPost() {
    val createDMReplyUseCase by inject<CreateDMReplyUseCase>()

    post("{dmId}/{messageId}") {
        call.getTwoParameter<String, String>("dmId", "messageId")
            .flatMap { (dmId, messageId) ->
                call.getRequest<DMsShowMessagesShowPostRequest>()
                    .map { (body) ->
                        createDMReplyUseCase(
                            dmId = DMId(dmId),
                            dmMessageId = DMMessageId(messageId),
                            body = DMBody(body),
                        )
                    }
            }
            .mapBoth(
                success = { DMsShowMessagesShowPostResponse(isSuccess = true) },
                failure = { ExceptionSerializable.from(it) },
            )
            .consume(
                success = { response -> call.respond(response) },
                failure = { (exception, status) -> call.respond(status, exception.message) },
            )
    }
}

@Serializable
private data class DMsShowMessagesShowPostRequest(
    val body: String
)

@Serializable
private data class DMsShowMessagesShowPostResponse(val isSuccess: Boolean)
