package com.vb4.dm

import com.vb4.avatar.Avatar
import com.vb4.result.flatMap
import com.vb4.result.map
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class CreateDMReplyUseCase(
    private val dmRepository: DMRepository,
    private val dmMessageRepository: DMMessageRepository,
    private val dispatcher: CoroutineDispatcher = Dispatchers.Default,
) {
    suspend operator fun invoke(
        dmId: DMId,
        dmMessageId: DMMessageId,
        body: DMBody,
    ) = withContext(dispatcher){
        dmRepository
            .getDM(dmId)
            .flatMap { dm ->
                dmMessageRepository
                    .getDMMessage(dm, dmMessageId)
                    .map { message -> dm to message }
            }
            .map { (dm, message) ->
                dm to DMReply.create(
                    from = Avatar.reconstruct(dm.userEmail),
                    body = body,
                    parent = message,
                )
            }
            .map { (dm, reply) -> dmMessageRepository.insertDMReply(dm, reply) }
    }
}
