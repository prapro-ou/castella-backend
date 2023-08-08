package com.vb4.routing

import com.vb4.DomainException
import io.ktor.http.HttpStatusCode
import kotlinx.serialization.Serializable

@Serializable
class ExceptionSerializable(
    val message: String,
) {
    companion object {
        fun from(exception: DomainException): Pair<ExceptionSerializable, HttpStatusCode> =
            when (exception) {
                is DomainException.NoSuchElementException ->
                    ExceptionSerializable(exception.message) to HttpStatusCode.BadRequest
                is DomainException.RequestValidationException ->
                    ExceptionSerializable(exception.message) to HttpStatusCode.BadRequest
                is DomainException.SystemException ->
                    ExceptionSerializable(exception.message) to HttpStatusCode.InternalServerError
            }
    }
}
