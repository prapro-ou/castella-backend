package com.vb4.plugins

import com.vb4.dm.CreateDMMessageUseCase
import com.vb4.dm.CreateDMReplyUseCase
import com.vb4.dm.CreateDMUseCase
import com.vb4.dm.DMMessageRepository
import com.vb4.dm.GetDMMessageByIdUseCase
import com.vb4.dm.GetDMMessagesByDMIdUseCase
import com.vb4.group.CreateGroupMessageUseCase
import com.vb4.group.CreateGroupReplyUseCase
import com.vb4.group.CreateGroupUseCase
import com.vb4.group.GetGroupMessageByIdUseCase
import com.vb4.group.GetGroupMessagesByGroupIdUseCase
import com.vb4.group.GroupMessageRepository
import com.vb4.mail.smtp.Smtp
import com.vb4.repository.DMMessageRepositoryImpl
import com.vb4.repository.GroupMessageRepositoryImpl
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
        factory<GetUserDestinationsUseCase> {
            GetUserDestinationsUseCase(
                dmRepository = get(),
                groupRepository = get(),
            )
        }

        // DM
        factory<GetDMMessagesByDMIdUseCase> { (authUser: User.AuthUser) ->
            GetDMMessagesByDMIdUseCase(
                dmRepository = get(),
                dmMessageRepository = getDMMessageRepository(database = get(), authUser = authUser),
            )
        }
        factory<CreateDMUseCase> { CreateDMUseCase(dmRepository = get()) }

        factory<GetDMMessageByIdUseCase> { (authUser: User.AuthUser) ->
            GetDMMessageByIdUseCase(
                dmRepository = get(),
                dmMessageRepository = getDMMessageRepository(database = get(), authUser = authUser),
            )
        }
        factory<CreateDMMessageUseCase> { (authUser: User.AuthUser) ->
            CreateDMMessageUseCase(
                dmRepository = get(),
                dmMessageRepository = getDMMessageRepository(database = get(), authUser = authUser),
            )
        }
        factory<CreateDMReplyUseCase> { (authUser: User.AuthUser) ->
            CreateDMReplyUseCase(
                dmRepository = get(),
                dmMessageRepository = getDMMessageRepository(database = get(), authUser = authUser),
            )
        }

        // Group
        factory<GetGroupMessagesByGroupIdUseCase> { (authUser: User.AuthUser) ->
            GetGroupMessagesByGroupIdUseCase(
                groupRepository = get(),
                groupMessageRepository = getGroupMessageRepository(database = get(), authUser = authUser),
            )
        }
        factory<CreateGroupUseCase> { CreateGroupUseCase(groupRepository = get()) }
        factory<GetGroupMessageByIdUseCase> { (authUser: User.AuthUser) ->
            GetGroupMessageByIdUseCase(
                groupRepository = get(),
                groupMessageRepository = getGroupMessageRepository(database = get(), authUser = authUser),
            )
        }
        factory<CreateGroupMessageUseCase> { (authUser: User.AuthUser) ->
            CreateGroupMessageUseCase(
                groupRepository = get(),
                groupMessageRepository = getGroupMessageRepository(database = get(), authUser = authUser),
            )
        }
        factory<CreateGroupReplyUseCase> { (authUser: User.AuthUser) ->
            CreateGroupReplyUseCase(
                groupRepository = get(),
                groupMessageRepository = getGroupMessageRepository(database = get(), authUser = authUser),
            )
        }

        /*** Repository ***/
        single<UserRepository> { UserRepositoryImpl(database = get()) }

        single<Database> { DevDB }
    }

    install(Koin) { modules(module) }
}

private fun getDMMessageRepository(
    database: Database,
    authUser: User.AuthUser,
): DMMessageRepository =
    DMMessageRepositoryImpl(
        database = database,
        smtp = getSmtp(authUser),
    )

private fun getGroupMessageRepository(
    authUser: User.AuthUser,
    database: Database,
): GroupMessageRepository =
    GroupMessageRepositoryImpl(
        database = database,
        smtp = getSmtp(authUser),
    )

private fun getSmtp(authUser: User.AuthUser) = Smtp.Gmail(
    user = authUser.email.value,
    password = authUser.password.value,
)
