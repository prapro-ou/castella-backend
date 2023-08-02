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

internal val fakeDestinationData = List(4) {
    Destination.DM(
        id = DestinationId("DestinationId$it"),
        first = Email("sample1@example.com"),
        second = Email("sample${it + 1}@example.com"),
    )
}

internal val fakeMessageData = List(4) {
    Message(
        id = MessageId("MessageId$it"),
        sender = fakeUserData[it - 1],
        subject = Subject("Subject$it"),
        body = Body("Body$it"),
        createdAt = CreatedAt(Clock.System.now()),
    )
} + List(4) {
    Reply(
        id = MessageId("MessageId${it + 4}"),
        inReplyTo = MessageId("MessageId$it"),
        sender = fakeUserData[4],
        subject = Subject("ReplySubject$it"),
        body = Body("ReplyBody$it"),
        createdAt = CreatedAt(Clock.System.now()),
    )
}

internal val fakeUserData = List(4) { User(Email("sample${it + 1}@example.com"), listOf()) } + User(Email("sample1@example.com"), fakeDestinationData)
