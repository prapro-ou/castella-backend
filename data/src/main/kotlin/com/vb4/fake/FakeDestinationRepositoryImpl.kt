package com.vb4.fake

import com.vb4.DomainException
import com.vb4.result.ApiResult
import com.vb4.destination.Destination
import com.vb4.destination.DestinationId
import com.vb4.destination.DestinationRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import repository.com.vb4.runCatchDomainException

class FakeDestinationRepositoryImpl(
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
) : DestinationRepository {
    override suspend fun getDestination(
        destinationId: DestinationId,
    ): ApiResult<Destination, DomainException> = withContext(dispatcher) {
        runCatchDomainException {
            fakeDMData.first { (it as? Destination.DM)?.id == destinationId }
        }
    }
}
