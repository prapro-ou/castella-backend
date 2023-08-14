package com.vb4.user

import com.vb4.Email

// アプリケーションの利用者を指す
sealed interface User {
    class RegisterUser private constructor(
        val email: Email,
        val loginPassword: LoginPassword,
        val mailPassword: MailPassword,
    ) {
        companion object {
            fun create(email: Email, loginPassword: LoginPassword, mailPassword: MailPassword) =
                RegisterUser(
                    email = email,
                    loginPassword = loginPassword,
                    mailPassword = mailPassword,
                )
        }
    }
    class BeforeAuthUser private constructor(val email: Email, val password: LoginPassword) : User {
        companion object {
            fun reconstruct(email: Email, password: LoginPassword) = BeforeAuthUser(
                email = email,
                password = password
            )
        }
    }
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
