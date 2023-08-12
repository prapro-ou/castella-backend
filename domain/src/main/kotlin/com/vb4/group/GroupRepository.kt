package com.vb4.group

import com.vb4.DomainException
import com.vb4.dm.DMId
import com.vb4.dm.DMMessage
import com.vb4.message.MessageId
import com.vb4.result.ApiResult

interface GroupRepository {
    suspend fun getGroup(groupId: GroupId): ApiResult<Group, DomainException>

    suspend fun getGroupMessages(groupId: GroupId): ApiResult<List<GroupMessage>, DomainException>

    suspend fun getGroupMessage(groupId: GroupId, messageId: GroupMessageId): ApiResult<GroupMessage, DomainException>
}
