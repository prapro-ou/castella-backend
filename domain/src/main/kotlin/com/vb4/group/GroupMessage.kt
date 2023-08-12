package com.vb4.group

import com.vb4.avatar.Avatar
import com.vb4.dm.DMBody
import com.vb4.dm.DMCreatedAt
import com.vb4.dm.DMMessage
import com.vb4.dm.DMMessageId
import com.vb4.dm.DMReply
import com.vb4.dm.DMSubject
import com.vb4.generateId
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant

class GroupMessage private constructor(
    val id: GroupMessageId,
    val from: Avatar,
    val subject: GroupSubject,
    val body: GroupBody,
    val replies: List<GroupReply>,
    val createdAt: GroupCreatedAt,
) {
    companion object {
        fun create(
            from: Avatar,
            subject: GroupSubject,
            body: GroupBody,
        ) = GroupMessage(
            id = GroupMessageId(generateId()),
            from = from,
            subject = subject,
            body = body,
            replies = listOf(),
            createdAt = GroupCreatedAt(Clock.System.now())
        )

        fun reconstruct(
            id: GroupMessageId,
            from: Avatar,
            subject: GroupSubject,
            body: GroupBody,
            replies: List<GroupReply>,
            createdAt: GroupCreatedAt,
        ) = GroupMessage(
            id = id,
            from = from,
            subject = subject,
            body = body,
            replies = replies,
            createdAt = createdAt,
        )
    }
}

class GroupReply private constructor(
    val id: GroupMessageId,
    val from: Avatar,
    val subject: GroupSubject,
    val body: GroupBody,
    val createdAt: GroupCreatedAt,
) {
    companion object {
        fun create(
            from: Avatar,
            subject: GroupSubject,
            body: GroupBody,
        ) = GroupReply(
            id = GroupMessageId(generateId()),
            from = from,
            subject = subject,
            body = body,
            createdAt = GroupCreatedAt(Clock.System.now())
        )

        fun reconstruct(
            id: GroupMessageId,
            from: Avatar,
            subject: GroupSubject,
            body: GroupBody,
            replies: List<GroupReply>,
            createdAt: GroupCreatedAt,
        ) = GroupReply(
            id = id,
            from = from,
            subject = subject,
            body = body,
            createdAt = createdAt,
        )
    }
}

@JvmInline
value class GroupMessageId(val value: String)

@JvmInline
value class GroupSubject(val value: String)

@JvmInline
value class GroupBody(val value: String)

@JvmInline
value class GroupCreatedAt(val value: Instant)
