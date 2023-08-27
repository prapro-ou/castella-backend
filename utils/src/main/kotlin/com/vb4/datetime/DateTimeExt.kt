package com.vb4.datetime

import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toJavaLocalDateTime
import kotlinx.datetime.toKotlinLocalDateTime
import kotlinx.datetime.toLocalDateTime
import java.time.LocalDateTime

val DEFAULT_TIME_ZONE = TimeZone.UTC

fun LocalDateTime.toKotlinInstant(timeZone: TimeZone = DEFAULT_TIME_ZONE) = this
    .toKotlinLocalDateTime()
    .toInstant(timeZone)

fun Instant.toJavaLocalDateTime(timeZone: TimeZone = DEFAULT_TIME_ZONE) = this
    .toLocalDateTime(timeZone)
    .toJavaLocalDateTime()

fun Instant.toDisplayString() = this
    .toLocalDateTime(DEFAULT_TIME_ZONE)
    .let { "${it.date}" }