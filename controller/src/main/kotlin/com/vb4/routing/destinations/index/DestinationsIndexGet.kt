package com.vb4.routing.destinations.index

import com.vb4.Email
import com.vb4.dm.DM
import com.vb4.group.Group
import com.vb4.result.consume
import com.vb4.result.mapBoth
import com.vb4.routing.ExceptionSerializable
import com.vb4.user.GetUserDestinationsUseCase
import io.ktor.server.application.call
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import kotlinx.serialization.Serializable
import org.koin.ktor.ext.inject

fun Route.destinationsIndexGet() {
    val getDestinationsUseCase by inject<GetUserDestinationsUseCase>()

    get {
        getDestinationsUseCase(email = Email("inputUserEmail"))
            .mapBoth(
                success = { (dm, group) -> GetDestinationIndexResponse.from(dm, group) },
                failure = { ExceptionSerializable.from(it) },
            ).consume(
                success = { response -> call.respond(response) },
                failure = { (exception, code) -> call.respond(code, exception.message) },
            )
    }
}

@Serializable
private data class GetDestinationIndexResponse(
    val dm: List<DestinationSerializable>,
    val group: List<DestinationSerializable>,
) {
    companion object {
        fun from(dm: List<DM>, group: List<Group>) =
            GetDestinationIndexResponse(
                dm = dm.map { DestinationSerializable.from(it) },
                group = group.map { DestinationSerializable.from(it) },
            )
    }
}

@Serializable
private data class DestinationSerializable(
    val id: String,
    val name: String,
) {
    companion object {
        fun from(dm: DM) = DestinationSerializable(
            id = dm.id.value,
            name = dm.name.value,
        )

        fun from(group: Group) = DestinationSerializable(
            id = group.id.value,
            name = group.name.value,
        )
    }
}
