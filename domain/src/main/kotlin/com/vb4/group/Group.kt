package com.vb4.group

import com.vb4.avatar.Avatar

data class  Group(
    val id: GroupId,
    val name: GroupName,
    val to: List<Avatar>,
)

@JvmInline
value class GroupId(val value: String)

@JvmInline
value class GroupName(val value: String)
