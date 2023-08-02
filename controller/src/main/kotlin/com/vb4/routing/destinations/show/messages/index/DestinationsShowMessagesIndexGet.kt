package com.vb4.routing.destinations.show.messages.index

import GetMessagesByDestinationUseCase
import com.vb4.ext.getParameter
import com.vb4.result.consume
import com.vb4.result.flatMap
import com.vb4.result.mapBoth
import com.vb4.serializable.ExceptionSerializable
import destination.DestinationId
import io.ktor.server.application.call
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import kotlinx.serialization.Serializable
import org.koin.ktor.ext.inject

fun Route.destinationsShowMessagesIndexGet(path: String, destinationIdParam: String) {
    val getMessagesByDestinationUseCase by inject<GetMessagesByDestinationUseCase>()

    get(path) {
        call.getParameter<String>(destinationIdParam)
            .flatMap { id -> getMessagesByDestinationUseCase(DestinationId(id)) }
            .mapBoth(
                success = { messages -> GetDestinationShowResponse(messages.map { it.id.value }) },
                failure = { ExceptionSerializable.from(it) },
            )
            .consume(
                success = { response -> call.respond(response) },
                failure = { (exception, status) -> call.respond(status, exception.message) },
            )
    }
}

@Serializable
data class GetDestinationShowResponse(
    val mails: List<String>,
)
