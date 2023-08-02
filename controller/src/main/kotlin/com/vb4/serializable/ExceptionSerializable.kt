package com.vb4.serializable

import DomainException
import io.ktor.http.HttpStatusCode
import kotlinx.serialization.Serializable

@Serializable
class ExceptionSerializable(
    val message: String,
) {
    companion object {
        fun from(exception: DomainException): Pair<ExceptionSerializable, HttpStatusCode> =
            when (exception) {
                is DomainException.SystemException ->
                    ExceptionSerializable(exception.message) to HttpStatusCode.InternalServerError
            }
    }
}
