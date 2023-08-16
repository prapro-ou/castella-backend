package com.vb4.group

import com.vb4.Email
import com.vb4.avatar.Avatar
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class CreateGroupUseCase(
    private val groupRepository: GroupRepository,
    private val dispatcher: CoroutineDispatcher = Dispatchers.Default,
) {
    suspend operator fun invoke(
        name: GroupName,
        userEmail: Email,
        to: List<Avatar>,
    ) = withContext(dispatcher) {
        Group.create(name = name, userEmail = userEmail, to = to)
            .let { group -> groupRepository.insertGroup(group) }
    }
}