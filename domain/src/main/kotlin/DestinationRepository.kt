import com.vb4.result.ApiResult

interface DestinationRepository {
    suspend fun getDestination(destinationId: DestinationId): ApiResult<Destination, DomainException>
}
