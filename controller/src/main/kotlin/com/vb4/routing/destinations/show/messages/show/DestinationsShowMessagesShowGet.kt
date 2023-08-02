package com.vb4.routing.destinations.show.messages.show

import GetMessageByIdUseCase
import com.vb4.ext.getParameter
import com.vb4.result.consume
import com.vb4.result.flatMap
import com.vb4.result.mapBoth
import com.vb4.serializable.ExceptionSerializable
import io.ktor.server.application.call
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import message.MessageId
import org.koin.ktor.ext.inject

fun Route.destinationsShowMessagesShowGet(path: String, messageIdParam: String) {
    val getMessageByIdUseCase by inject<GetMessageByIdUseCase>()

    get(path) {
        call.getParameter<String>(messageIdParam)
            .flatMap { messageId -> getMessageByIdUseCase(MessageId(messageId)) }
            .mapBoth(
                success = { message -> message.id },
                failure = { ExceptionSerializable.from(it) },
            )
            .consume(
                success = { response -> call.respond(response) },
                failure = { (exception, status) -> call.respond(status, exception.message) },
            )
    }
}
