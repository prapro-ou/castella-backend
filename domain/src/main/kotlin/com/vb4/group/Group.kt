package com.vb4.group

import com.vb4.Email
import com.vb4.NewMessageCount
import com.vb4.avatar.Avatar
import com.vb4.generateId

class Group private constructor(
    val id: GroupId,
    val name: GroupName,
    val userEmail: Email,
    val to: List<Avatar>,
    val newMessageCount: NewMessageCount,
) {
    companion object {
        fun create(name: GroupName, userEmail: Email, to: List<Avatar>) = Group(
            id = GroupId(generateId()),
            name = name,
            userEmail = userEmail,
            to = to,
            newMessageCount = NewMessageCount(0),
        )

        fun reconstruct(
            id: GroupId,
            name: GroupName,
            userEmail: Email,
            to: List<Avatar>,
            newMessageCount: NewMessageCount,
        ) = Group(id = id, name = name, userEmail = userEmail, to = to, newMessageCount = newMessageCount)
    }
}

@JvmInline
value class GroupId(val value: String)

@JvmInline
value class GroupName(val value: String)
