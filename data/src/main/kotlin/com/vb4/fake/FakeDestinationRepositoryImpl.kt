package com.vb4.fake

import com.vb4.DomainException
import com.vb4.dm.DM
import com.vb4.result.ApiResult
import com.vb4.dm.DMId
import com.vb4.dm.DMRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import repository.com.vb4.runCatchDomainException

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
}
