package com.vb4.plugins

import com.vb4.dm.CreateDMMessageUseCase
import com.vb4.dm.CreateDMReplyUseCase
import com.vb4.dm.CreateDMUseCase
import com.vb4.dm.DMMessageRepository
import com.vb4.dm.GetDMMessageByIdUseCase
import com.vb4.user.GetUserDestinationsUseCase
import com.vb4.dm.GetDMMessagesByDMIdUseCase
import com.vb4.group.GetGroupMessagesByGroupIdUseCase
import com.vb4.dm.DMRepository
import com.vb4.group.GetGroupMessageByIdUseCase
import com.vb4.group.GroupRepository
import com.vb4.mail.imap.Imap
import com.vb4.mail.smtp.Smtp
import com.vb4.repository.DMMessageRepositoryImpl
import com.vb4.repository.DMRepositoryImpl
import com.vb4.repository.GroupRepositoryImpl
import com.vb4.repository.UserRepositoryImpl
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
        // Destination
        single<GetUserDestinationsUseCase> { GetUserDestinationsUseCase(get(), get()) }

        // DM
        single<GetDMMessagesByDMIdUseCase> { GetDMMessagesByDMIdUseCase(get(), get()) }
        single<CreateDMUseCase> { CreateDMUseCase(get()) }

        single<GetDMMessageByIdUseCase> { GetDMMessageByIdUseCase(get(), get()) }
        single<CreateDMMessageUseCase> { CreateDMMessageUseCase(get(), get()) }
        single<CreateDMReplyUseCase> { CreateDMReplyUseCase(get(), get()) }

        // Group
        single<GetGroupMessagesByGroupIdUseCase> { GetGroupMessagesByGroupIdUseCase(get(), get()) }
        single<GetGroupMessageByIdUseCase> { GetGroupMessageByIdUseCase(get(), get()) }

        /*** Repository ***/
        single<DMRepository> { DMRepositoryImpl(get()) }
        single<GroupRepository> { GroupRepositoryImpl(get()) }
        single<UserRepository> { UserRepositoryImpl(get()) }
        single<DMMessageRepository> { DMMessageRepositoryImpl(get(), get()) }
        single<Imap> { Imap.Gmail("inputUserEmail", "") }
        single<Smtp> { Smtp.Gmail("inputUserEmail", "") }

        single<Database> { DevDB }
    }

    install(Koin) { modules(module) }
}
