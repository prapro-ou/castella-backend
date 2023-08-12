package com.vb4.dm

import com.vb4.avatar.Avatar
import com.vb4.generateId
import java.util.UUID
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant

class DMMessage private constructor(
    val id: DMMessageId,
    val from: Avatar,
    val subject: DMSubject,
    val body: DMBody,
    val replies: List<DMReply>,
    val createdAt: DMCreatedAt,
) {
    companion object {
        fun create(
            from: Avatar,
            subject: DMSubject,
            body: DMBody,
        ) = DMMessage(
            id = DMMessageId(generateId()),
            from = from,
            subject = subject,
            body = body,
            replies = listOf(),
            createdAt = DMCreatedAt(Clock.System.now())
        )

        fun reconstruct(
            id: DMMessageId,
            from: Avatar,
            subject: DMSubject,
            body: DMBody,
            replies: List<DMReply>,
            createdAt: DMCreatedAt,
        ) = DMMessage(
            id = id,
            from = from,
            subject = subject,
            body = body,
            replies = replies,
            createdAt = createdAt,
        )
    }
}

class DMReply private constructor(
    val id: DMMessageId,
    val from: Avatar,
    val subject: DMSubject,
    val body: DMBody,
    val createdAt: DMCreatedAt,
) {
    companion object {
        fun create(
            from: Avatar,
            body: DMBody,
            parent: DMMessage,
        ) = DMReply(
            id = DMMessageId(generateId()),
            from = from,
            subject = DMSubject("Re: ${parent.subject.value}"),
            body = body,
            createdAt = DMCreatedAt(Clock.System.now())
        )

        fun reconstruct(
            id: DMMessageId,
            from: Avatar,
            subject: DMSubject,
            body: DMBody,
            createdAt: DMCreatedAt,
        ) = DMReply(
            id = id,
            from = from,
            subject = subject,
            body = body,
            createdAt = createdAt,
        ) }
}

@JvmInline
value class DMMessageId(val value: String)

@JvmInline
value class DMSubject(val value: String)

@JvmInline
value class DMBody(val value: String)

@JvmInline
value class DMCreatedAt(val value: Instant)
