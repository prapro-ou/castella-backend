package message

import DomainException
import com.vb4.result.ApiResult
import destination.Destination

interface MessageRepository {
    suspend fun getMessages(destination: Destination): ApiResult<List<Message>, DomainException>

    suspend fun getMessageWithReplies(messageId: MessageId): ApiResult<Message, DomainException>
}
