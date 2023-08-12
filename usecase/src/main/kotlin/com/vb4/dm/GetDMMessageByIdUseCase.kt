package com.vb4.dm

import com.vb4.DomainException
import com.vb4.result.ApiResult
import com.vb4.result.flatMap
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GetDMMessageByIdUseCase(
    private val dmRepository: DMRepository,
    private val dmMessageRepository: DMMessageRepository,
    private val dispatcher: CoroutineDispatcher = Dispatchers.Default,
) {
    suspend operator fun invoke(dmId: DMId, messageId: DMMessageId): ApiResult<DMMessage, DomainException> =
        withContext(dispatcher) {
            dmRepository.getDM(dmId)
                .flatMap { dm -> dmMessageRepository.getDMMessage(dm, messageId) }
        }
}
