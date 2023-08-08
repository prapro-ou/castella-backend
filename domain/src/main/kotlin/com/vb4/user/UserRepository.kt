package com.vb4.user

import com.vb4.DomainException
import com.vb4.result.ApiResult

interface UserRepository {
    suspend fun getUser(email: Email): ApiResult<User, DomainException>
}
