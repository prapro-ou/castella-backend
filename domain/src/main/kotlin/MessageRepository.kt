import com.vb4.result.ApiResult

interface MessageRepository {
    suspend fun getMessages(destination: Destination): ApiResult<List<Message>, DomainException>

    suspend fun getMessageWithReplies(messageId: MessageId): ApiResult<List<Message>, DomainException>
}
