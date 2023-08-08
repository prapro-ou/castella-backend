package com.vb4.message

import com.vb4.DomainException
import com.vb4.result.ApiResult
import com.vb4.destination.Destination

interface MessageRepository {
    suspend fun getMessagesByDestination(destination: Destination): ApiResult<List<Message>, DomainException>

    suspend fun getMessageById(messageId: MessageId): ApiResult<Message, DomainException>
}
