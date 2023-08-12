package com.vb4.user

import com.vb4.Email
import com.vb4.avatar.Avatar
import com.vb4.dm.DM
import com.vb4.group.Group

// アプリケーションの利用者を指す
class User private constructor(
    val email: Email,
) {
    companion object {
        fun reconstruct(email: Email) = User(email)
    }
}
