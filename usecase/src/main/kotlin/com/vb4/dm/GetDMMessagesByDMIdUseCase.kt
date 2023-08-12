package com.vb4.dm

import com.vb4.DomainException
import com.vb4.result.ApiResult
import com.vb4.result.flatMap
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GetDMMessagesByDMIdUseCase(
    private val dmRepository: DMRepository,
    private val dispatcher: CoroutineDispatcher = Dispatchers.Default,
) {
    suspend operator fun invoke(dmId: DMId): ApiResult<List<DMMessage>, DomainException> =
        withContext(dispatcher) {
            dmRepository
                .getDM(dmId)
                .flatMap { dm -> dmRepository.getDMMessages(dm) }
        }
}
