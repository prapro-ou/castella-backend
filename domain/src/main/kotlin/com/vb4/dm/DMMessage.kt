package com.vb4.dm

import com.vb4.avatar.Avatar
import com.vb4.generateId
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant

class DMMessage private constructor(
    val id: DMMessageId,
    val from: Avatar,
    val subject: DMSubject,
    val body: DMBody,
    val replies: List<DMReply>,
    val isRecent: Boolean,
    val newMessageCount: Int,
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
            isRecent = false,
            newMessageCount = 0,
            createdAt = DMCreatedAt(Clock.System.now()),
        )

        fun reconstruct(
            id: DMMessageId,
            from: Avatar,
            subject: DMSubject,
            body: DMBody,
            replies: List<DMReply>,
            isRecent: Boolean,
            newMessageCount: Int,
            createdAt: DMCreatedAt,
        ) = DMMessage(
            id = id,
            from = from,
            subject = subject,
            body = body,
            replies = replies,
            isRecent = isRecent,
            newMessageCount = newMessageCount,
            createdAt = createdAt,
        )
    }
}

class DMReply private constructor(
    val id: DMMessageId,
    val from: Avatar,
    val subject: DMSubject,
    val body: DMBody,
    val isRecent: Boolean,
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
            isRecent = false,
            createdAt = DMCreatedAt(Clock.System.now()),
        )

        fun reconstruct(
            id: DMMessageId,
            from: Avatar,
            subject: DMSubject,
            body: DMBody,
            isRecent: Boolean,
            createdAt: DMCreatedAt,
        ) = DMReply(
            id = id,
            from = from,
            subject = subject,
            body = body,
            isRecent = isRecent,
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
