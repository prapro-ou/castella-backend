package com.vb4.dm

import com.vb4.avatar.Avatar
import com.vb4.Email

data class DM(
    val id: DMId,
    val name: DMName,
    val userEmail: Email,
    val to: Avatar,
)

@JvmInline
value class DMId(val value: String)

@JvmInline
value class DMName(val value: String)
