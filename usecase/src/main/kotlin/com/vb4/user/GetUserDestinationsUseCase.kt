package com.vb4.user

import com.vb4.DomainException
import com.vb4.dm.DM
import com.vb4.result.ApiResult
import com.vb4.result.map
import com.vb4.group.Group
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import com.vb4.Email
import com.vb4.dm.DMRepository
import com.vb4.group.GroupRepository
import com.vb4.result.flatMap
import kotlinx.coroutines.async

class GetUserDestinationsUseCase(
    private val dmRepository: DMRepository,
    private val groupRepository: GroupRepository,
    private val dispatcher: CoroutineDispatcher = Dispatchers.Default,
) {
    suspend operator fun invoke(
        email: Email,
    ): ApiResult<Pair<List<DM>, List<Group>>, DomainException> =
        withContext(dispatcher) {
            val dms = async { dmRepository.getDMsByUserEmail(email) }
            val groups = async { groupRepository.getGroupsByUserEmail(email) }
            dms.await().flatMap { dm: List<DM> ->
                groups.await().map { group: List<Group> -> dm to group }
            }
        }
}
