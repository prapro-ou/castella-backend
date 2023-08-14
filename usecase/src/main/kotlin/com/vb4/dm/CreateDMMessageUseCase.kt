package com.vb4.dm

import com.vb4.avatar.Avatar
import com.vb4.result.map
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class CreateDMMessageUseCase(
    private val dmRepository: DMRepository,
    private val dmMessageRepository: DMMessageRepository,
    private val dispatcher: CoroutineDispatcher = Dispatchers.Default,
) {
    suspend operator fun invoke(
        dmId: DMId,
        subject: DMSubject,
        body: DMBody,
    ) = withContext(dispatcher) {
        dmRepository
            .getDM(dmId)
            .map { dm ->
                dm to DMMessage.create(
                    from = Avatar.reconstruct(dm.userEmail),
                    subject = subject,
                    body = body,
                )
            }
            .map { (dm, message) -> dmMessageRepository.insertDMMessage(dm, message) }
    }
}
