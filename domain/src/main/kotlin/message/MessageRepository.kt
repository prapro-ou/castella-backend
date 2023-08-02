package message

import DomainException
import com.vb4.result.ApiResult
import destination.Destination

interface MessageRepository {
    suspend fun getMessagesByDestination(destination: Destination): ApiResult<List<Message>, DomainException>

    suspend fun getMessageById(messageId: MessageId): ApiResult<Message, DomainException>
}
