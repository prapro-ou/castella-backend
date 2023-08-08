package com.vb4.fake

import com.vb4.DomainException
import com.vb4.result.ApiResult
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import repository.com.vb4.runCatchDomainException
import com.vb4.user.Email
import com.vb4.user.User
import com.vb4.user.UserRepository

class FakeUserRepositoryImpl(
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
) : UserRepository {
    override suspend fun getUser(email: Email): ApiResult<User, DomainException> =
        withContext(dispatcher) {
            runCatchDomainException {
                fakeUserData.first { it.email == email }
            }
        }
}
