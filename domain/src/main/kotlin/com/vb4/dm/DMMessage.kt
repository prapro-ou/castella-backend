package com.vb4.dm

import com.vb4.avatar.Avatar
import kotlinx.datetime.Instant

data class DMMessage(
    val id: DMMessageId,
    val from: Avatar,
    val subject: DMSubject,
    val body: DMBody,
    val replies: List<DMReply>,
    val createdAt: DMCreatedAt,
)

data class DMReply(
    val id: DMMessageId,
    val from: Avatar,
    val subject: DMSubject,
    val body: DMBody,
    val createdAt: DMCreatedAt,
)

@JvmInline
value class DMMessageId(val value: String)

@JvmInline
value class DMSubject(val value: String)

@JvmInline
value class DMBody(val value: String)

@JvmInline
value class DMCreatedAt(val value: Instant)
