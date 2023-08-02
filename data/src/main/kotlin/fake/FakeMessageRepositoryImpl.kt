package repository.fake

import DomainException
import com.vb4.result.ApiResult
import destination.Destination
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import message.Message
import message.MessageId
import message.MessageRepository
import repository.runCatchDomainException

class FakeMessageRepositoryImpl(
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
) : MessageRepository {

    /*** 本来はIMAPを使って取ってくるところ ***/

    override suspend fun getMessages(destination: Destination): ApiResult<List<Message>, DomainException> =
        withContext(dispatcher) {
            runCatchDomainException {
                fakeMessageData.filter {
                    it.sender.email == (destination as Destination.DM).to.email
                }
            }
        }

    override suspend fun getMessageWithReplies(messageId: MessageId): ApiResult<Message, DomainException> =
        withContext(dispatcher) {
            runCatchDomainException {
                fakeMessageData.first { it.id == messageId }
            }
        }
}
