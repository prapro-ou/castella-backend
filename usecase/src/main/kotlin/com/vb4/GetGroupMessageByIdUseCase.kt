package com.vb4

import com.vb4.group.GroupId
import com.vb4.group.GroupMessage
import com.vb4.group.GroupMessageId
import com.vb4.group.GroupRepository
import com.vb4.result.ApiResult
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GetGroupMessageByIdUseCase(
    private val groupRepository: GroupRepository,
    private val dispatcher: CoroutineDispatcher = Dispatchers.Default,
) {
    suspend operator fun invoke(
        groupId: GroupId,
        messageId: GroupMessageId,
    ): ApiResult<GroupMessage, DomainException> =
        withContext(dispatcher) {
            groupRepository.getGroupMessage(groupId, messageId)
        }
}
