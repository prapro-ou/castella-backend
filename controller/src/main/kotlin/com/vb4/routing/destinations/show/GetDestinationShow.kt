package com.vb4.routing.destinations.show

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
import org.koin.ktor.ext.inject

fun Route.getDestinationShow(path: String, param: String) {
    val getMessagesByDestinationUseCase by inject<GetMessagesByDestinationUseCase>()

    get(path) {
        call.getParameter<String>(param)
            .flatMap { id -> getMessagesByDestinationUseCase(DestinationId(id)) }
            .mapBoth(
                success = { Unit },
                failure = { ExceptionSerializable.from(it) },
            )
            .consume(
                success = { room -> call.respond(room) },
                failure = { (exception, status) -> call.respond(status, exception.message) },
            )
    }
}
