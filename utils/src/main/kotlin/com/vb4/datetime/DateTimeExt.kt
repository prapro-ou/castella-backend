package com.vb4.datetime

import java.time.LocalDateTime
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toJavaLocalDateTime
import kotlinx.datetime.toKotlinLocalDateTime
import kotlinx.datetime.toLocalDateTime

fun LocalDateTime.toKotlinInstant(timeZone: TimeZone = TimeZone.UTC) = this
    .toKotlinLocalDateTime()
    .toInstant(timeZone)

fun Instant.toJavaLocalDateTime(timeZone: TimeZone = TimeZone.UTC) = this
    .toLocalDateTime(timeZone)
    .toJavaLocalDateTime()