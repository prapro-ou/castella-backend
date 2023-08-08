package repository.fake

import avatar.Avatar
import destination.Destination
import destination.DestinationId
import destination.DestinationName
import kotlinx.datetime.Clock
import message.Body
import message.CreatedAt
import message.Message
import message.MessageId
import message.Reply
import message.Subject
import user.Email
import user.User

/*** DBに登録する情報 ***/
internal val fakeDMData: List<Destination.DM> = listOf(
    Destination.DM(
        id = DestinationId("DestinationId1"),
        name = DestinationName("DestinationName1"),
        to = Avatar(Email("sample2@example.com")),
    ),
    Destination.DM(
        id = DestinationId("DestinationId2"),
        name = DestinationName("DestinationName2"),
        to = Avatar(Email("sample3@example.com")),
    ),
    Destination.DM(
        id = DestinationId("DestinationId3"),
        name = DestinationName("DestinationName3"),
        to = Avatar(Email("sample4@example.com")),
    ),
    Destination.DM(
        id = DestinationId("DestinationId4"),
        name = DestinationName("DestinationName4"),
        to = Avatar(Email("sample5@example.com")),
    ),
)

internal val fakeUserData: List<User> = listOf(User(Email("sample1@example.com"), fakeDMData, listOf()))

/*** Emailを用いてネットから拾ってくる情報 ***/
internal val fakeAvatar: List<Avatar> = listOf(
    Avatar(Email("sample1@example.com")),
    Avatar(Email("sample2@example.com")),
    Avatar(Email("sample3@example.com")),
    Avatar(Email("sample4@example.com")),
)

internal val fakeMessageData: List<Message> = listOf(
    Message(
        id = MessageId("MessageId1"),
        sender = Avatar(Email("sample2@example.com")),
        subject = Subject("Subject1"),
        body = Body("Body1"),
        replies = listOf(
            Reply(
                id = MessageId("MessageId4"),
                sender = Avatar(Email("sample1@example.com")),
                subject = Subject("ReplySubject1"),
                body = Body("ReplyBody1"),
                createdAt = CreatedAt(Clock.System.now()),
            ),
        ),
        createdAt = CreatedAt(Clock.System.now()),
    ),
    Message(
        id = MessageId("MessageId2"),
        sender = Avatar(Email("sample3@example.com")),
        subject = Subject("Subject2"),
        body = Body("Body2"),
        replies = listOf(),
        createdAt = CreatedAt(Clock.System.now()),
    ),
    Message(
        id = MessageId("MessageId3"),
        sender = Avatar(Email("sample4@example.com")),
        subject = Subject("Subject3"),
        body = Body("Body3"),
        replies = listOf(),
        createdAt = CreatedAt(Clock.System.now()),
    ),
)
