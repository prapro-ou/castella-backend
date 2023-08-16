package com.vb4.group

import com.vb4.avatar.Avatar
import com.vb4.result.flatMap
import com.vb4.result.map
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class CreateGroupMessageUseCase(
    private val groupRepository: GroupRepository,
    private val groupMessageRepository: GroupMessageRepository,
    private val dispatcher: CoroutineDispatcher = Dispatchers.Default,
) {
    suspend operator fun invoke(
        groupId: GroupId,
        subject: GroupSubject,
        body: GroupBody,
    ) = withContext(dispatcher) {
        groupRepository
            .getGroup(groupId)
            .map { group ->
                group to GroupMessage.create(
                    from = Avatar.reconstruct(group.userEmail),
                    subject = subject,
                    body = body,
                )
            }
            .map { (group, message) -> groupMessageRepository.insertGroupMessage(group, message) }
    }
}