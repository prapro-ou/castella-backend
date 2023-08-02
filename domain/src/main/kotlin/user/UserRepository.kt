package user

import DomainException
import com.vb4.result.ApiResult

interface UserRepository {
    suspend fun getUser(email: Email): ApiResult<User, DomainException>
}
