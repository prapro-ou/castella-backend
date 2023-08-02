interface MessageRepository {
    suspend fun getAllMessages(): List<Message>

    suspend fun getMessagesByEmail(email: Email): List<Message>
}
