package message

import kotlinx.datetime.Instant
import user.User

open class Message(
    val id: MessageId,
    val sender: User,
    val subject: Subject,
    val body: Body,
    val createdAt: CreatedAt,
)

class Reply(
    id: MessageId,
    inReplyTo: MessageId,
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
