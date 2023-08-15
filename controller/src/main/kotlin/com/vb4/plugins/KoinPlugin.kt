package com.vb4.plugins

import com.vb4.dm.CreateDMMessageUseCase
import com.vb4.dm.CreateDMReplyUseCase
import com.vb4.dm.CreateDMUseCase
import com.vb4.dm.DMMessageRepository
import com.vb4.dm.DMRepository
import com.vb4.dm.GetDMMessageByIdUseCase
import com.vb4.dm.GetDMMessagesByDMIdUseCase
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
        single<GetUserDestinationsUseCase> {
            GetUserDestinationsUseCase(dmRepository = get(), groupRepository = get())
        }

        // DM
        single<GetDMMessagesByDMIdUseCase> { (authUser: User.AuthUser) ->
            GetDMMessagesByDMIdUseCase(
                dmRepository = get(),
                dmMessageRepository = getDMMessageRepository(authUser),
            )
        }
        single<CreateDMUseCase> { CreateDMUseCase(dmRepository = get()) }

        single<GetDMMessageByIdUseCase> { (authUser: User.AuthUser) ->
            GetDMMessageByIdUseCase(
                dmRepository = get(),
                dmMessageRepository = getDMMessageRepository(authUser),
            )
        }
        single<CreateDMMessageUseCase> { (authUser: User.AuthUser) ->
            CreateDMMessageUseCase(
                dmRepository = get(),
                dmMessageRepository = getDMMessageRepository(authUser),
            )
        }
        single<CreateDMReplyUseCase> { (authUser: User.AuthUser) ->
            CreateDMReplyUseCase(
                dmRepository = get(),
                dmMessageRepository = getDMMessageRepository(authUser),
            )
        }

        // Group
        single<GetGroupMessagesByGroupIdUseCase> { (authUser: User.AuthUser) ->
            GetGroupMessagesByGroupIdUseCase(
                groupRepository = get(),
                groupMessageRepository = getGroupMessageRepository(authUser),
            )
        }
        single<GetGroupMessageByIdUseCase> { (authUser: User.AuthUser) ->
            GetGroupMessageByIdUseCase(
                groupRepository = get(),
                groupMessageRepository = getGroupMessageRepository(authUser),
            )
        }

        /*** Repository ***/
        single<DMRepository> { DMRepositoryImpl(database = get()) }
        single<GroupRepository> { GroupRepositoryImpl(database = get()) }
        single<UserRepository> { UserRepositoryImpl(database = get()) }

        single<Database> { DevDB }
    }

    install(Koin) { modules(module) }
}



private fun getDMMessageRepository(
    authUser: User.AuthUser,
): DMMessageRepository =
    DMMessageRepositoryImpl(
        Imap.Gmail(authUser.email.value, authUser.password.value),
        Smtp.Gmail(authUser.email.value, authUser.password.value)
    )

private fun getGroupMessageRepository(
    authUser: User.AuthUser,
): GroupMessageRepository =
    GroupMessageRepositoryImpl(
        Imap.Gmail(authUser.email.value, authUser.password.value),
    )