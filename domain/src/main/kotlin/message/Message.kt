package message

import user.User
import kotlinx.datetime.Instant

open class Message(
    val id: MessageId,
    val sender: User,
    val subject: Subject,
    val body: Body,
    val createdAt: CreatedAt,
)

class Reply(
    id: MessageId,
    parent: MessageId,
    sender: User,
    subject: Subject,
    body: Body,
    createdAt: CreatedAt,
) : Message(id, sender, subject, body, createdAt)

@JvmInline
value class MessageId(val value: String)

@JvmInline
value class Subject(val value: String)

@JvmInline
value class Body(val value: String)

@JvmInline
value class CreatedAt(val value: Instant)
