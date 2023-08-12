package com.vb4

import com.vb4.dm.DMId
import com.vb4.dm.DMMessage
import com.vb4.dm.DMRepository
import com.vb4.result.ApiResult
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GetMessagesByDMIdUseCase(
    private val dmRepository: DMRepository,
    private val dispatcher: CoroutineDispatcher = Dispatchers.Default,
) {
    suspend operator fun invoke(dmId: DMId): ApiResult<List<DMMessage>, DomainException> =
        withContext(dispatcher) {
            dmRepository.getDMMessages(dmId)
        }
}
