package com.vb4.plugins.auth

import com.auth0.jwt.JWTCreator
import com.auth0.jwt.interfaces.Payload
import com.vb4.Email

private const val EMAIL = "email"

fun JWTCreator.Builder.withEmail(email: Email): JWTCreator.Builder = withClaim(EMAIL, email.value)

val Payload.email get() = getClaim(EMAIL).asString()?.let { Email(it) }
