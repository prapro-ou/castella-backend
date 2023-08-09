package com.vb4

import com.vb4.result.ApiResult
import com.vb4.result.flatMap
import com.vb4.dm.DMId
import com.vb4.group.GroupId
import com.vb4.group.GroupRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import com.vb4.message.Message
import com.vb4.message.MessageRepository

class GetMessagesByGroupIdUseCase(
    private val groupRepository: GroupRepository,
    private val messageRepository: MessageRepository,
    private val dispatcher: CoroutineDispatcher = Dispatchers.Default,
) {
    suspend operator fun invoke(groupId: GroupId): ApiResult<List<Message>, DomainException> =
        withContext(dispatcher) {
            groupRepository
                .getGroup(groupId)
                .flatMap { group -> messageRepository.getMessagesByGroup(group) }
        }
}
