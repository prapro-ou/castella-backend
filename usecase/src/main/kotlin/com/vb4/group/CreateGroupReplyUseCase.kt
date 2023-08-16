package com.vb4.group

import com.vb4.avatar.Avatar
import com.vb4.result.flatMap
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
        groupMessageId: GroupMessageId,
        body: GroupBody,
    ) = withContext(dispatcher) {
        groupRepository
            .getGroup(groupId)
            .flatMap { group ->
                groupMessageRepository
                    .getGroupMessage(group, groupMessageId)
                    .map { message -> group to message }
            }
            .map { (group, message) ->
                group to GroupReply.create(
                    from = Avatar.reconstruct(group.userEmail),
                    body = body,
                    parent = message,
                )
            }
            .map { (group, reply) -> groupMessageRepository.insertGroupReply(group, reply) }
    }
}
