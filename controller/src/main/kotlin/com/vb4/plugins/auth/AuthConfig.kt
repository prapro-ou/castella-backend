package com.vb4.plugins.auth

import com.vb4.DomainException
import io.ktor.server.config.ApplicationConfig

val ApplicationConfig.secret get() = property("ktor.jwt.secret").getString()
val ApplicationConfig.audience get() = property("ktor.jwt.audience").getString()
val ApplicationConfig.issuer get() = property("ktor.jwt.issuer").getString()
val ApplicationConfig.castellaRealm get() = property("ktor.jwt.realm").getString()
val ApplicationConfig.expiredSecond get() =
    property("ktor.jwt.expiredSecond")
        .getString()
        .toLongOrNull()
        ?: throw DomainException.SystemException("Config [expiredAt] is not number.", null)
