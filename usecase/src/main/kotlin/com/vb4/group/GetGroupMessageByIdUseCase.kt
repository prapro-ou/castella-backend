package com.vb4.group

import com.vb4.DomainException
import com.vb4.result.ApiResult
import com.vb4.result.flatMap
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GetGroupMessageByIdUseCase(
    private val groupRepository: GroupRepository,
    private val groupMessageRepository: GroupMessageRepository,
    private val dispatcher: CoroutineDispatcher = Dispatchers.Default,
) {
    suspend operator fun invoke(
        groupId: GroupId,
        messageId: GroupMessageId,
    ): ApiResult<GroupMessage, DomainException> =
        withContext(dispatcher) {
            groupRepository.getGroup(groupId)
                .flatMap { group -> groupMessageRepository.getGroupMessage(group, messageId) }
        }
}
