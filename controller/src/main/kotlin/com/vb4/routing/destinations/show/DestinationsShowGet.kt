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
import kotlinx.serialization.Serializable
import org.koin.ktor.ext.inject

fun Route.destinationsShowGet() {
    val getMessagesByDestinationUseCase by inject<GetMessagesByDestinationUseCase>()

    get("{destinationId}") {
        call.getParameter<String>("destinationId")
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
    val messages: List<String>,
)
