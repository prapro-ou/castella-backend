import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class GetAllUserUseCase(
    private val messageRepository: MessageRepository,
    private val dispatcher: CoroutineDispatcher,
) {
    suspend operator fun invoke() = withContext(dispatcher) {
        messageRepository.getAllMessages().filter { it.sender.email == Email("") }
    }
}
