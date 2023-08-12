package com.vb4.dm

import com.vb4.DomainException
import com.vb4.result.ApiResult

interface DMRepository {
    suspend fun getDM(dmId: DMId): ApiResult<DM, DomainException>

    suspend fun getDMMessages(dmId: DMId): ApiResult<List<DMMessage>, DomainException>

    suspend fun getDMMessage(dmId: DMId, messageId: DMMessageId): ApiResult<DMMessage, DomainException>
}
