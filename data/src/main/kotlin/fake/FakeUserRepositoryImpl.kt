package repository.fake

import DomainException
import com.vb4.result.ApiResult
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import repository.runCatchDomainException
import user.Email
import user.User
import user.UserRepository

class FakeUserRepositoryImpl(private val dispatcher: CoroutineDispatcher) : UserRepository {
    override suspend fun getUser(email: Email): ApiResult<User, DomainException> =
        withContext(dispatcher) {
            runCatchDomainException {
                fakeUserData.first { it.email == email }
            }
        }
}
