package com.vb4.routing

import com.vb4.DomainException
import com.vb4.result.ApiResult
import io.ktor.server.application.ApplicationCall
import io.ktor.server.plugins.BadRequestException
import io.ktor.server.plugins.CannotTransformContentToTypeException
import io.ktor.server.request.receive

inline fun <reified T> ApplicationCall.getParameter(
    param: String,
    errorMessage: String = "validation error",
): ApiResult<T, DomainException> =
    (this.parameters[param] as? T)
        ?.let { ApiResult.Success(it) }
        ?: ApiResult.Failure(DomainException.RequestValidationException(errorMessage))

inline fun <reified T, reified R> ApplicationCall.getTwoParameter(
    param1: String,
    param2: String,
    errorMessage: String = "validation error",
): ApiResult<Pair<T, R>, DomainException> {
    val first = this.parameters[param1] as? T
    val second = this.parameters[param2] as? R
    return when {
        first == null || second == null -> ApiResult.Failure(DomainException.RequestValidationException(errorMessage))
        else -> ApiResult.Success(first to second)
    }
}

suspend inline fun <reified T : Any> ApplicationCall.getRequest(
    errorMessage: String = "validation error",
): ApiResult<T, DomainException> =
    try {
        ApiResult.Success(this.receive())
    } catch (e: CannotTransformContentToTypeException) {
        ApiResult.Failure(DomainException.RequestValidationException(errorMessage))
    } catch (e: BadRequestException) {
        ApiResult.Failure(DomainException.RequestValidationException(errorMessage))
    }
