package com.vb4

import com.vb4.dm.DMId
import com.vb4.dm.DMMessage
import com.vb4.dm.DMMessageId
import com.vb4.dm.DMRepository
import com.vb4.result.ApiResult
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GetDMMessageByIdUseCase(
    private val messageRepository: DMRepository,
    private val dispatcher: CoroutineDispatcher = Dispatchers.Default,
) {
    suspend operator fun invoke(dmId: DMId, messageId: DMMessageId): ApiResult<DMMessage, DomainException> =
        withContext(dispatcher) {
            messageRepository.getDMMessage(dmId, messageId)
        }
}
