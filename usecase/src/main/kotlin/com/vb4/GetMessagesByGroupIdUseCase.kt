package com.vb4

import com.vb4.result.ApiResult
import com.vb4.group.GroupId
import com.vb4.group.GroupMessage
import com.vb4.group.GroupRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GetMessagesByGroupIdUseCase(
    private val groupRepository: GroupRepository,
    private val dispatcher: CoroutineDispatcher = Dispatchers.Default,
) {
    suspend operator fun invoke(groupId: GroupId): ApiResult<List<GroupMessage>, DomainException> =
        withContext(dispatcher) {
            groupRepository.getGroupMessages(groupId)
        }
}
