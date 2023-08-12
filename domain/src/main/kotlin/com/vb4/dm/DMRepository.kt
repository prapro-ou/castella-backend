package com.vb4.dm

import com.vb4.DomainException
import com.vb4.Email
import com.vb4.result.ApiResult

interface DMRepository {

    suspend fun getDMsByUserEmail(userEmail: Email): ApiResult<List<DM>, DomainException>
    suspend fun getDM(dmId: DMId): ApiResult<DM, DomainException>
}
