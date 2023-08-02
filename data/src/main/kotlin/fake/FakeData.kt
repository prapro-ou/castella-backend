package repository.fake

import destination.Destination
import destination.DestinationId
import kotlinx.datetime.Clock
import message.Body
import message.CreatedAt
import message.Message
import message.MessageId
import message.Reply
import message.Subject
import user.Email
import user.User

internal val fakeDestinationData: List<Destination> = List(4) {
    Destination.DM(
        id = DestinationId("DestinationId$it"),
        user = Email("sample0@example.com"),
        to = Email("sample${it + 1}@example.com"),
    )
}

internal val fakeUserData: List<User> = List(4) { User(Email("sample${it + 1}@example.com"), listOf()) } +
    User(Email("sample0@example.com"), fakeDestinationData)

internal val fakeMessageData: List<Message> = List(4) {
    Message(
        id = MessageId("MessageId$it"),
        sender = fakeUserData[it + 1],
        subject = Subject("Subject$it"),
        body = Body("Body$it"),
        createdAt = CreatedAt(Clock.System.now()),
    )
} + List(4) {
    Reply(
        id = MessageId("MessageId${it + 4}"),
        inReplyTo = MessageId("MessageId$it"),
        sender = fakeUserData.last(),
        subject = Subject("ReplySubject$it"),
        body = Body("ReplyBody$it"),
        createdAt = CreatedAt(Clock.System.now()),
    )
}
