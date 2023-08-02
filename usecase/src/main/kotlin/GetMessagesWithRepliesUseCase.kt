import com.vb4.result.ApiResult
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import message.Message
import message.MessageId
import message.MessageRepository

class GetMessagesWithRepliesUseCase(
    private val messageRepository: MessageRepository,
    private val dispatcher: CoroutineDispatcher = Dispatchers.Default,
) {
    suspend operator fun invoke(messageId: MessageId): ApiResult<Message, DomainException> =
        withContext(dispatcher) {
            messageRepository.getMessageWithReplies(messageId)
        }
}
