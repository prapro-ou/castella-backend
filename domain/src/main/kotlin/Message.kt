import kotlinx.datetime.Instant

data class Message(
    val id: MessageId,
    val sender: User,
    val subject: Subject,
    val body: Body,
    val createdAt: CreatedAt,
)

@JvmInline
value class MessageId(val value: String)

@JvmInline
value class Subject(val value: String)

@JvmInline
value class Body(val value: String)

@JvmInline
value class CreatedAt(val value: Instant)
