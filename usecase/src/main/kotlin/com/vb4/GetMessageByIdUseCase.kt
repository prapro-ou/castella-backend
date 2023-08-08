package com.vb4

import com.vb4.result.ApiResult
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import com.vb4.message.Message
import com.vb4.message.MessageId
import com.vb4.message.MessageRepository

class GetMessageByIdUseCase(
    private val messageRepository: MessageRepository,
    private val dispatcher: CoroutineDispatcher = Dispatchers.Default,
) {
    suspend operator fun invoke(messageId: MessageId): ApiResult<Message, DomainException> =
        withContext(dispatcher) {
            messageRepository.getMessageById(messageId)
        }
}
