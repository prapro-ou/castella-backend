import com.vb4.result.ApiResult
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import message.Message
import message.MessageId
import message.MessageRepository

class GetMessagesWithRepliesUseCase(
    private val messageRepository: MessageRepository,
    private val dispatcher: CoroutineDispatcher,
) {
    suspend operator fun invoke(messageId: MessageId): ApiResult<List<Message>, DomainException> =
        withContext(dispatcher) {
            messageRepository.getMessageWithReplies(messageId)
        }
}
