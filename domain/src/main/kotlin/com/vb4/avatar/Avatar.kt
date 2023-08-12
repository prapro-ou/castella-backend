package com.vb4.avatar

import com.vb4.Email

// メールアドレスに紐づく情報を指す
class Avatar private constructor(
    val email: Email,
) {
    companion object {
        fun reconstruct(email: Email) = Avatar(email)
    }
}
