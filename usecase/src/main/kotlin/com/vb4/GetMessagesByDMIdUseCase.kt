package com.vb4

import com.vb4.result.ApiResult
import com.vb4.result.flatMap
import com.vb4.dm.DMId
import com.vb4.dm.DMRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import com.vb4.message.Message
import com.vb4.message.MessageRepository

class GetMessagesByDMIdUseCase(
    private val dmRepository: DMRepository,
    private val messageRepository: MessageRepository,
    private val dispatcher: CoroutineDispatcher = Dispatchers.Default,
) {
    suspend operator fun invoke(dmId: DMId): ApiResult<List<Message>, DomainException> =
        withContext(dispatcher) {
            dmRepository
                .getDM(dmId)
                .flatMap { dm -> messageRepository.getMessagesByDM(dm) }
        }
}
