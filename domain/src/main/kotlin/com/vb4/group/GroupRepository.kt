package com.vb4.group

import com.vb4.DomainException
import com.vb4.result.ApiResult

interface GroupRepository {
    suspend fun getGroup(groupId: GroupId): ApiResult<Group, DomainException>
}
