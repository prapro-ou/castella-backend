package com.vb4.user

import com.vb4.Email

// アプリケーションの利用者を指す
sealed interface User {
    class BeforeAuthUser(val email: Email, val password: LoginPassword) : User
    class AuthUser private constructor(val email: Email, val password: MailPassword) : User {
        companion object {
            fun reconstruct(email: Email, password: MailPassword) = AuthUser(
                email = email,
                password = password,
            )
        }
    }
}

@JvmInline
value class LoginPassword(val value: String)

@JvmInline
value class MailPassword(val value: String)
