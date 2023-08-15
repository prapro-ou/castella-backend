package com.vb4.routing.dms.show

import com.vb4.dm.CreateDMMessageUseCase
import com.vb4.dm.DMBody
import com.vb4.dm.DMId
import com.vb4.dm.DMSubject
import com.vb4.plugins.auth.authUser
import com.vb4.result.consume
import com.vb4.result.flatMap
import com.vb4.result.map
import com.vb4.result.mapBoth
import com.vb4.routing.ExceptionSerializable
import com.vb4.routing.getParameter
import com.vb4.routing.getRequest
import io.ktor.server.application.call
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.post
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.koin.core.parameter.parametersOf
import org.koin.ktor.ext.inject

fun Route.dmsShowPost() {

    post("{dmId}") {
        val createDMMessageUseCase by this@dmsShowPost
            .inject<CreateDMMessageUseCase> { parametersOf(call.authUser) }

        call.getParameter<String>("dmId")
            .flatMap { dmId ->
                call.getRequest<DMsShowPostRequest>()
                    .map { (subject, body) ->
                        createDMMessageUseCase(
                            dmId = DMId(dmId),
                            subject = DMSubject(subject),
                            body = DMBody(body),
                        )
                    }
            }
            .mapBoth(
                success = { DMsShowPostResponse(isSuccess = true) },
                failure = { ExceptionSerializable.from(it) },
            )
            .consume(
                success = { response -> call.respond(response) },
                failure = { (exception, status) -> call.respond(status, exception.message) },
            )
    }
}

@Serializable
private data class DMsShowPostRequest(val subject: String, val body: String)

@Serializable
private data class DMsShowPostResponse(@SerialName("is_success") val isSuccess: Boolean)
