import com.vb4.result.ApiResult

interface MessageRepository {
    suspend fun getAllMessages(user: User): ApiResult<List<Message>, DomainException>

    // TODO:より良い命名
    suspend fun getMessages(user: User, destination: Destination): ApiResult<List<Message>, DomainException>
}
