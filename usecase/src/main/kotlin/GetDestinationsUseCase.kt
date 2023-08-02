import com.vb4.result.ApiResult
import com.vb4.result.map
import destination.Destination
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import user.Email
import user.UserRepository

class GetDestinationsUseCase(
    private val userRepository: UserRepository,
    private val dispatcher: CoroutineDispatcher,
) {
    suspend operator fun invoke(email: Email): ApiResult<List<Destination>, DomainException> =
        withContext(dispatcher) {
            userRepository.getUser(email).map { it.destinations }
        }
}
