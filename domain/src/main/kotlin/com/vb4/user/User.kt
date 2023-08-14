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

sealed interface TempUser {
    class BeforeAuthUser(val email: Email, val password: LoginPassword) : TempUser
    class AuthUser(val email: Email, val password: MailPassword) : TempUser
}

@JvmInline
value class LoginPassword(val value: String)

@JvmInline
value class MailPassword(val value: String)
