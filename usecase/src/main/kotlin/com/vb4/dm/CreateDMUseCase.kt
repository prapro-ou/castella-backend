package com.vb4.dm

import com.vb4.Email
import com.vb4.avatar.Avatar
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class CreateDMUseCase(
    private val dmRepository: DMRepository,
    private val dispatcher: CoroutineDispatcher = Dispatchers.Default,
) {
    suspend operator fun invoke(name: DMName, userEmail: Email, to: Avatar) =
        withContext(dispatcher) {
            DM.create(name = name, userEmail = userEmail, to = to)
                .let { dm -> dmRepository.insertDM(dm) }
        }
}