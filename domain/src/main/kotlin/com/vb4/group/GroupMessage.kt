package com.vb4.group

import com.vb4.NewMessageCount
import com.vb4.avatar.Avatar
import com.vb4.generateId
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant

class GroupMessage private constructor(
    val id: GroupMessageId,
    val from: Avatar,
    val subject: GroupSubject,
    val body: GroupBody,
    val isRecent: Boolean,
    val newMessageCount: NewMessageCount,
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
            isRecent = false,
            newMessageCount = NewMessageCount(0),
            createdAt = GroupCreatedAt(Clock.System.now()),
        )

        fun reconstruct(
            id: GroupMessageId,
            from: Avatar,
            subject: GroupSubject,
            body: GroupBody,
            isRecent: Boolean,
            newMessageCount: NewMessageCount,
            replies: List<GroupReply>,
            createdAt: GroupCreatedAt,
        ) = GroupMessage(
            id = id,
            from = from,
            subject = subject,
            body = body,
            isRecent = isRecent,
            newMessageCount = newMessageCount,
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
    val isRecent: Boolean,
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
            isRecent = false,
            createdAt = GroupCreatedAt(Clock.System.now()),
        )

        fun reconstruct(
            id: GroupMessageId,
            from: Avatar,
            subject: GroupSubject,
            body: GroupBody,
            isRecent: Boolean,
            createdAt: GroupCreatedAt,
        ) = GroupReply(
            id = id,
            from = from,
            subject = subject,
            body = body,
            isRecent = isRecent,
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
