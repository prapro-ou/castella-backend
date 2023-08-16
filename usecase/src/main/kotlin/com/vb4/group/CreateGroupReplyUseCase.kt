package com.vb4.group

import com.vb4.avatar.Avatar
import com.vb4.result.map
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class CreateGroupReplyUseCase(
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
                group to GroupReply.create(
                    from = Avatar.reconstruct(group.userEmail),
                    subject = subject,
                    body = body,
                )
            }
            .map { (group, reply) -> groupMessageRepository.insertGroupReply(group, reply) }
    }
}