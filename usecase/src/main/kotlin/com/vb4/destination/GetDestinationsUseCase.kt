package com.vb4.destination

import com.vb4.DomainException
import com.vb4.dm.DM
import com.vb4.result.ApiResult
import com.vb4.result.map
import com.vb4.group.Group
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import com.vb4.Email
import com.vb4.user.UserRepository

class GetDestinationsUseCase(
    private val userRepository: UserRepository,
    private val dispatcher: CoroutineDispatcher = Dispatchers.Default,
) {
    suspend operator fun invoke(
        email: Email,
    ): ApiResult<Pair<List<DM>, List<Group>>, DomainException> =
        withContext(dispatcher) {
            userRepository.getUser(email).map { user -> user.dms to user.groups }
        }
}
