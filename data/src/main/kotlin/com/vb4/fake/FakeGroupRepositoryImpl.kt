package com.vb4.fake

import com.vb4.DomainException
import com.vb4.group.Group
import com.vb4.group.GroupId
import com.vb4.group.GroupRepository
import com.vb4.result.ApiResult
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import repository.com.vb4.runCatchDomainException

class FakeGroupRepositoryImpl(
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
) : GroupRepository {
    override suspend fun getGroup(groupId: GroupId): ApiResult<Group, DomainException> =
        withContext(dispatcher) {
            runCatchDomainException {
                throw DomainException.NoSuchElementException("")
            }
        }
}
