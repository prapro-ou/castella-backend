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
import message.Reply
import repository.runCatchDomainException

class FakeMessageRepositoryImpl(
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
) : MessageRepository {
    override suspend fun getMessages(destination: Destination): ApiResult<List<Message>, DomainException> =
        withContext(dispatcher) {
            runCatchDomainException {
                fakeMessageData.filter {
                    it.sender.email == (destination as Destination.DM).first || it.sender.email == destination.second
                }
            }
        }

    override suspend fun getMessageWithReplies(messageId: MessageId): ApiResult<List<Message>, DomainException> =
        withContext(dispatcher) {
            runCatchDomainException {
                listOf(fakeMessageData.first { it.id == messageId }) +
                    fakeMessageData.filterIsInstance<Reply>().filter { it.inReplyTo == messageId }
            }
        }
}
