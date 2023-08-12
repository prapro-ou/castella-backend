package com.vb4.repository

import com.vb4.DomainException
import com.vb4.group.Group
import com.vb4.group.GroupMessage
import com.vb4.group.GroupMessageId
import com.vb4.group.GroupMessageRepository
import com.vb4.mail.imap.Imap
import com.vb4.result.ApiResult
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

class GroupMessageRepositoryImpl(
    private val imap: Imap,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
) : GroupMessageRepository {
    override suspend fun getGroupMessages(group: Group): ApiResult<List<GroupMessage>, DomainException> {
        TODO("Not yet implemented")
    }

    override suspend fun getGroupMessage(
        group: Group,
        messageId: GroupMessageId
    ): ApiResult<GroupMessage, DomainException> {
        TODO("Not yet implemented")
    }
}
