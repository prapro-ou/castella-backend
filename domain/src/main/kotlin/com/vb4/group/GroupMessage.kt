package com.vb4.group

import com.vb4.avatar.Avatar
import kotlinx.datetime.Instant

data class GroupMessage(
    val id: GroupMessageId,
    val from: Avatar,
    val subject: GroupSubject,
    val body: GroupBody,
    val replies: List<GroupReply>,
    val createdAt: GroupCreatedAt,
)

data class GroupReply(
    val id: GroupMessageId,
    val from: Avatar,
    val subject: GroupSubject,
    val body: GroupBody,
    val createdAt: GroupCreatedAt,
)

@JvmInline
value class GroupMessageId(val value: String)

@JvmInline
value class GroupSubject(val value: String)

@JvmInline
value class GroupBody(val value: String)

@JvmInline
value class GroupCreatedAt(val value: Instant)
