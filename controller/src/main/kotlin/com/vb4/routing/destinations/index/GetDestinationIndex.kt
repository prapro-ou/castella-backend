package com.vb4.routing.destinations.index

import GetDestinationsUseCase
import com.vb4.result.consume
import com.vb4.result.mapBoth
import com.vb4.serializable.ExceptionSerializable
import destination.Destination
import destination.Destination.Companion.divide
import io.ktor.server.application.call
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import kotlinx.serialization.Serializable
import org.koin.ktor.ext.inject
import user.Email

fun Route.getDestinationIndex(path: String) {
    val getDestinationsUseCase by inject<GetDestinationsUseCase>()

    get(path) {
        getDestinationsUseCase(email = Email("sample1@example.com"))
            .mapBoth(
                success = { destinations ->
                    val (dm, group) = destinations.divide()
                    GetDestinationIndexResponse(
                        dm = dm.map { DestinationSerializable.from(it) },
                        group = group.map { DestinationSerializable.from(it) },
                    )
                },
                failure = { ExceptionSerializable.from(it) },
            ).consume(
                success = { destinations -> call.respond(destinations) },
                failure = { (exception, code) -> call.respond(code, exception.message) },
            )
    }
}

@Serializable
data class GetDestinationIndexResponse(
    val dm: List<DestinationSerializable>,
    val group: List<DestinationSerializable>,
)

@Serializable
data class DestinationSerializable(
    val id: String,
    val name: String,
) {
    companion object {
        fun from(destination: Destination) = DestinationSerializable(
            id = destination.id.value,
            name = destination.name.value,
        )
    }
}
