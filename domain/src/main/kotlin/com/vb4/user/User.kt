package com.vb4.user

import com.vb4.Email
import com.vb4.dm.DM
import com.vb4.group.Group

// アプリケーションの利用者を指す
data class User(
    val email: Email,
)
