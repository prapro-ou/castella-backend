import com.vb4.result.ApiResult
import com.vb4.result.flatMap
import destination.DestinationId
import destination.DestinationRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import message.Message
import message.MessageRepository

class GetMessagesByDestinationUseCase(
    private val destinationRepository: DestinationRepository,
    private val messageRepository: MessageRepository,
    private val dispatcher: CoroutineDispatcher = Dispatchers.Default,
) {
    suspend operator fun invoke(destinationId: DestinationId): ApiResult<List<Message>, DomainException> =
        withContext(dispatcher) {
            destinationRepository
                .getDestination(destinationId)
                .flatMap { destination -> messageRepository.getMessages(destination) }
        }
}
