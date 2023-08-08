package com.vb4.message

import com.vb4.DomainException
import com.vb4.dm.DM
import com.vb4.result.ApiResult
import com.vb4.group.Group

interface MessageRepository {
    suspend fun getMessagesByDM(dm: DM): ApiResult<List<Message>, DomainException>

    suspend fun getMessagesByGroup(group: Group): ApiResult<List<Message>, DomainException>

    suspend fun getMessageById(messageId: MessageId): ApiResult<Message, DomainException>
}
