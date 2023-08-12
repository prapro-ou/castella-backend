package com.vb4.fake

import com.vb4.DomainException
import com.vb4.dm.DM
import com.vb4.dm.DMId
import com.vb4.dm.DMMessage
import com.vb4.dm.DMMessageId
import com.vb4.dm.DMRepository
import com.vb4.result.ApiResult
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import com.vb4.runCatchDomainException

class FakeDMRepositoryImpl(
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
) : DMRepository {
    override suspend fun getDM(
        dmId: DMId,
    ): ApiResult<DM, DomainException> = withContext(dispatcher) {
        runCatchDomainException {
            fakeDMData.first { it.id == dmId }
        }
    }

    override suspend fun getDMMessages(dmId: DMId): ApiResult<List<DMMessage>, DomainException> {
        TODO("Not yet implemented")
    }

    override suspend fun getDMMessage(dmId: DMId, messageId: DMMessageId): ApiResult<DMMessage, DomainException> {
        TODO("Not yet implemented")
    }
}
