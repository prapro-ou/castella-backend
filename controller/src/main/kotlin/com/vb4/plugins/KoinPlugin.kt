package com.vb4.plugins

import com.vb4.dm.CreateDMMessageUseCase
import com.vb4.dm.CreateDMReplyUseCase
import com.vb4.dm.CreateDMUseCase
import com.vb4.dm.DMMessageRepository
import com.vb4.dm.DMRepository
import com.vb4.dm.GetDMMessageByIdUseCase
import com.vb4.dm.GetDMMessagesByDMIdUseCase
import com.vb4.group.CreateGroupMessageUseCase
import com.vb4.group.CreateGroupReplyUseCase
import com.vb4.group.CreateGroupUseCase
import com.vb4.group.GetGroupMessageByIdUseCase
import com.vb4.group.GetGroupMessagesByGroupIdUseCase
import com.vb4.group.GroupMessageRepository
import com.vb4.group.GroupRepository
import com.vb4.mail.imap.Imap
import com.vb4.mail.smtp.Smtp
import com.vb4.repository.DMMessageRepositoryImpl
import com.vb4.repository.DMRepositoryImpl
import com.vb4.repository.GroupMessageRepositoryImpl
import com.vb4.repository.GroupRepositoryImpl
import com.vb4.repository.UserRepositoryImpl
import com.vb4.user.AuthUserUseCase
import com.vb4.user.GetUserDestinationsUseCase
import com.vb4.user.GetUserUseCase
import com.vb4.user.RegisterUserUseCase
import com.vb4.user.User
import com.vb4.user.UserRepository
import db.DevDB
import io.ktor.server.application.Application
import io.ktor.server.application.install
import org.jetbrains.exposed.sql.Database
import org.koin.dsl.module
import org.koin.ktor.plugin.Koin

fun Application.configureKoinPlugin() {
    val module = module {
        /*** UseCase ***/
        // User
        single<AuthUserUseCase> { AuthUserUseCase(userRepository = get()) }
        single<GetUserUseCase> { GetUserUseCase(userRepository = get()) }
        single<RegisterUserUseCase> { RegisterUserUseCase(userRepository = get()) }

        // Destination
        factory<GetUserDestinationsUseCase> { (authUser: User.AuthUser) ->
            GetUserDestinationsUseCase(
                dmRepository = getDMRepository(database = get(), authUser = authUser),
                groupRepository = get(),
            )
        }

        // DM
        factory<GetDMMessagesByDMIdUseCase> { (authUser: User.AuthUser) ->
            GetDMMessagesByDMIdUseCase(
                dmRepository = getDMRepository(database = get(), authUser = authUser),
                dmMessageRepository = getDMMessageRepository(authUser),
            )
        }
        factory<CreateDMUseCase> { (authUser: User.AuthUser) ->
            CreateDMUseCase(dmRepository = getDMRepository(database = get(), authUser = authUser))
        }

        factory<GetDMMessageByIdUseCase> { (authUser: User.AuthUser) ->
            GetDMMessageByIdUseCase(
                dmRepository = getDMRepository(database = get(), authUser = authUser),
                dmMessageRepository = getDMMessageRepository(authUser),
            )
        }
        factory<CreateDMMessageUseCase> { (authUser: User.AuthUser) ->
            CreateDMMessageUseCase(
                dmRepository = getDMRepository(database = get(), authUser = authUser),
                dmMessageRepository = getDMMessageRepository(authUser),
            )
        }
        factory<CreateDMReplyUseCase> { (authUser: User.AuthUser) ->
            CreateDMReplyUseCase(
                dmRepository = getDMRepository(database = get(), authUser = authUser),
                dmMessageRepository = getDMMessageRepository(authUser),
            )
        }

        // Group
        factory<GetGroupMessagesByGroupIdUseCase> { (authUser: User.AuthUser) ->
            GetGroupMessagesByGroupIdUseCase(
                groupRepository = getGroupRepository(get(), authUser),
                groupMessageRepository = getGroupMessageRepository(authUser),
            )
        }
        factory<CreateGroupUseCase> { (authUser: User.AuthUser) ->
            CreateGroupUseCase(groupRepository = getGroupRepository(get(), authUser))
        }
        factory<GetGroupMessageByIdUseCase> { (authUser: User.AuthUser) ->
            GetGroupMessageByIdUseCase(
                groupRepository = getGroupRepository(get(), authUser),
                groupMessageRepository = getGroupMessageRepository(authUser),
            )
        }
        factory<CreateGroupMessageUseCase> { (authUser: User.AuthUser) ->
            CreateGroupMessageUseCase(
                groupRepository = getGroupRepository(get(), authUser),
                groupMessageRepository = getGroupMessageRepository(authUser),
            )
        }
        factory<CreateGroupReplyUseCase> { (authUser: User.AuthUser) ->
            CreateGroupReplyUseCase(
                groupRepository = getGroupRepository(get(), authUser),
                groupMessageRepository = getGroupMessageRepository(authUser),
            )
        }

        /*** Repository ***/
        single<UserRepository> { UserRepositoryImpl(database = get()) }

        single<Database> { DevDB }
    }

    install(Koin) { modules(module) }
}

private fun getDMRepository(
    database: Database,
    authUser: User.AuthUser,
): DMRepository =
    DMRepositoryImpl(
        database = database,
        imap = getImap(authUser),
    )

private fun getGroupRepository(
    database: Database,
    authUser: User.AuthUser,
): GroupRepository =
    GroupRepositoryImpl(
        database = database,
        imap = getImap(authUser),
    )

private fun getDMMessageRepository(
    authUser: User.AuthUser,
): DMMessageRepository =
    DMMessageRepositoryImpl(
        imap = getImap(authUser),
        smtp = getSmtp(authUser),
    )

private fun getGroupMessageRepository(
    authUser: User.AuthUser,
): GroupMessageRepository =
    GroupMessageRepositoryImpl(
        imap = getImap(authUser),
        smtp = getSmtp(authUser),
    )

private val imap: MutableMap<String, Imap> = mutableMapOf()

private fun getImap(authUser: User.AuthUser) = imap.getOrPut(authUser.email.value) {
    Imap.Gmail(
        user = authUser.email.value,
        password = authUser.password.value,
    )
}

private fun getSmtp(authUser: User.AuthUser) = Smtp.Gmail(
    user = authUser.email.value,
    password = authUser.password.value,
)
