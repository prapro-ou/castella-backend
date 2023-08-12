package com.vb4.dm

import com.vb4.avatar.Avatar
import com.vb4.Email
import com.vb4.generateId

class DM private constructor(
    val id: DMId,
    val name: DMName,
    val userEmail: Email,
    val to: Avatar,
) {
    companion object {
        fun create(name: DMName, userEmail: Email, to: Avatar) = DM(
            id = DMId(generateId()),
            name = name,
            userEmail = userEmail,
            to = to,
        )

        fun reconstruct(
            id: DMId,
            name: DMName,
            userEmail: Email,
            to: Avatar,
        ) = DM(id = id, name = name, userEmail = userEmail, to = to)
    }
}

@JvmInline
value class DMId(val value: String)

@JvmInline
value class DMName(val value: String)
