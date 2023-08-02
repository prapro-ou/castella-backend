package repository.fake

import DomainException
import com.vb4.result.ApiResult
import destination.Destination
import destination.DestinationId
import destination.DestinationRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import repository.runCatchDomainException
import user.Email

class FakeDestinationRepositoryImpl(
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
    ) : DestinationRepository {
    override suspend fun getDestination(
        destinationId: DestinationId,
    ): ApiResult<Destination, DomainException> = withContext(dispatcher) {
        runCatchDomainException {
            fakeDestinationData.first { it.id == destinationId }
        }
    }
}
