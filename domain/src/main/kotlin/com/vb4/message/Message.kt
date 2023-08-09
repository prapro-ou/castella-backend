package com.vb4.message

import com.vb4.avatar.Avatar
import kotlinx.datetime.Instant

data class Message(
    val id: MessageId,
    val from: Avatar,
    val subject: Subject,
    val body: Body,
    val replies: List<Reply>,
    val createdAt: CreatedAt,
)

data class Reply(
    val id: MessageId,
    val from: Avatar,
    val subject: Subject,
    val body: Body,
    val createdAt: CreatedAt,
)

@JvmInline
value class MessageId(val value: String)

@JvmInline
value class Subject(val value: String)

@JvmInline
value class Body(val value: String)

@JvmInline
value class CreatedAt(val value: Instant)
