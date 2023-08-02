package avatar

import DomainException
import com.vb4.result.ApiResult
import user.Email

interface AvatarRepository {
    suspend fun getAvatar(email: Email): ApiResult<Avatar, DomainException>
}
