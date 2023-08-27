package com.vb4.group

import com.vb4.DomainException
import com.vb4.result.ApiResult

interface GroupMessageRepository {
    suspend fun getGroupMessages(group: Group): ApiResult<List<GroupMessage>, DomainException>

    suspend fun getGroupMessage(group: Group, messageId: GroupMessageId): ApiResult<GroupMessage, DomainException>

    suspend fun insertGroupMessage(group: Group, message: GroupMessage): ApiResult<Unit, DomainException>

    suspend fun insertGroupReply(group: Group, inReplyTo: GroupMessageId, reply: GroupReply): ApiResult<Unit, DomainException>
}
