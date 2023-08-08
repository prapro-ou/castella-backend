package com.vb4.dm

import com.vb4.avatar.Avatar

data class DM(
    val id: DMId,
    val name: DMName,
    val to: Avatar,
)

@JvmInline
value class DMId(val value: String)

@JvmInline
value class DMName(val value: String)
