package com.vb4.fake

import com.vb4.DomainException
import com.vb4.dm.DM
import com.vb4.group.Group
import com.vb4.message.Message
import com.vb4.message.MessageId
import com.vb4.message.MessageRepository
import com.vb4.result.ApiResult
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import com.vb4.runCatchDomainException

class FakeMessageRepositoryImpl(
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
) : MessageRepository {

    /*** 本来はIMAPを使って取ってくるところ ***/

    override suspend fun getMessagesByDM(dm: DM): ApiResult<List<Message>, DomainException> =
        withContext(dispatcher) {
            runCatchDomainException {
                fakeMessageData.filter {
                    it.from.email == dm.to.email
                }
            }
        }

    override suspend fun getMessagesByGroup(group: Group): ApiResult<List<Message>, DomainException> =
        withContext(dispatcher) {
            runCatchDomainException {
                listOf()
            }
        }

    override suspend fun getMessageById(messageId: MessageId): ApiResult<Message, DomainException> =
        withContext(dispatcher) {
            runCatchDomainException {
                fakeMessageData.first { it.id == messageId }
            }
        }
}
