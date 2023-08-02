import com.vb4.result.ApiResult
import com.vb4.result.flatMap
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class GetMessagesByDestinationUseCase(
    private val destinationRepository: DestinationRepository,
    private val messageRepository: MessageRepository,
    private val dispatcher: CoroutineDispatcher,
) {
    suspend operator fun invoke(destinationId: DestinationId): ApiResult<List<Message>, DomainException> =
        withContext(dispatcher) {
            destinationRepository
                .getDestination(destinationId)
                .flatMap { destination -> messageRepository.getMessages(destination) }
        }
}
