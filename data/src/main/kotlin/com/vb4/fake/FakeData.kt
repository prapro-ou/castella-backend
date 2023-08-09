package com.vb4.fake

import com.vb4.Email
import com.vb4.avatar.Avatar
import com.vb4.dm.DM
import com.vb4.dm.DMId
import com.vb4.dm.DMName
import com.vb4.message.Body
import com.vb4.message.CreatedAt
import com.vb4.message.Message
import com.vb4.message.MessageId
import com.vb4.message.Reply
import com.vb4.message.Subject
import com.vb4.user.User
import kotlinx.datetime.Clock

/*** DBに登録する情報 ***/
internal val fakeDMData: List<DM> = listOf(
    DM(
        id = DMId("DestinationId1"),
        name = DMName("DestinationName1"),
        userEmail = Email("sample1@example.com"),
        to = Avatar(Email("sample2@example.com")),
    ),
    DM(
        id = DMId("DestinationId2"),
        name = DMName("DestinationName2"),
        userEmail = Email("sample1@example.com"),
        to = Avatar(Email("sample3@example.com")),
    ),
    DM(
        id = DMId("DestinationId3"),
        name = DMName("DestinationName3"),
        userEmail = Email("sample1@example.com"),
        to = Avatar(Email("sample4@example.com")),
    ),
    DM(
        id = DMId("DestinationId4"),
        name = DMName("DestinationName4"),
        userEmail = Email("sample1@example.com"),
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
        from = Avatar(Email("sample2@example.com")),
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
        from = Avatar(Email("sample3@example.com")),
        subject = Subject("Subject2"),
        body = Body("Body2"),
        replies = listOf(),
        createdAt = CreatedAt(Clock.System.now()),
    ),
    Message(
        id = MessageId("MessageId3"),
        from = Avatar(Email("sample4@example.com")),
        subject = Subject("Subject3"),
        body = Body("Body3"),
        replies = listOf(),
        createdAt = CreatedAt(Clock.System.now()),
    ),
)
