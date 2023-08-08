package com.vb4

import com.vb4.result.ApiResult
import com.vb4.result.flatMap
import com.vb4.destination.DestinationId
import com.vb4.destination.DestinationRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import com.vb4.message.Message
import com.vb4.message.MessageRepository

class GetMessagesByDestinationUseCase(
    private val destinationRepository: DestinationRepository,
    private val messageRepository: MessageRepository,
    private val dispatcher: CoroutineDispatcher = Dispatchers.Default,
) {
    suspend operator fun invoke(destinationId: DestinationId): ApiResult<List<Message>, DomainException> =
        withContext(dispatcher) {
            destinationRepository
                .getDestination(destinationId)
                .flatMap { destination -> messageRepository.getMessagesByDestination(destination) }
        }
}
