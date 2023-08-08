package com.vb4

import com.vb4.result.ApiResult
import com.vb4.result.map
import com.vb4.destination.Destination
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import com.vb4.user.Email
import com.vb4.user.UserRepository

class GetDestinationsUseCase(
    private val userRepository: UserRepository,
    private val dispatcher: CoroutineDispatcher = Dispatchers.Default,
) {
    suspend operator fun invoke(
        email: Email,
    ): ApiResult<Pair<List<Destination.DM>, List<Destination.Group>>, DomainException> =
        withContext(dispatcher) {
            userRepository.getUser(email).map { user -> user.dms to user.groups }
        }
}
