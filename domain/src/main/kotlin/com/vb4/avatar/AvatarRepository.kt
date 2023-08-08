package com.vb4.avatar

import com.vb4.DomainException
import com.vb4.result.ApiResult
import com.vb4.user.Email

interface AvatarRepository {
    suspend fun getAvatar(email: Email): ApiResult<Avatar, DomainException>
}
