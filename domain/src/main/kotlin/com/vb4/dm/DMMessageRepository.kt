package com.vb4.dm

import com.vb4.DomainException
import com.vb4.result.ApiResult

interface DMMessageRepository {
    suspend fun getDMMessages(dm: DM): ApiResult<List<DMMessage>, DomainException>

    suspend fun getDMMessage(dm: DM, messageId: DMMessageId): ApiResult<DMMessage, DomainException>

    suspend fun insertDMMessage(dm: DM, message: DMMessage): ApiResult<Unit, DomainException>
}