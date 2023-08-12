package com.vb4.group

import com.vb4.DomainException
import com.vb4.Email
import com.vb4.result.ApiResult

interface GroupRepository {

    suspend fun getGroupsByUserEmail(userEmail: Email): ApiResult<List<Group>, DomainException>

    suspend fun getGroup(groupId: GroupId): ApiResult<Group, DomainException>
}
