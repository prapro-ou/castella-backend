package com.vb4.user

import com.vb4.DomainException
import com.vb4.Email
import com.vb4.result.ApiResult

interface UserRepository {
    suspend fun getUser(email: Email): ApiResult<User.AuthUser, DomainException>

    suspend fun authUser(user: User.BeforeAuthUser): ApiResult<User.AuthUser, DomainException>

    suspend fun insertUser(user: User.RegisterUser): ApiResult<User.AuthUser, DomainException>
}
