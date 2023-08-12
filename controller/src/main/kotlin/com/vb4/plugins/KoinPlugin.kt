package com.vb4.plugins

import com.vb4.GetDestinationsUseCase
import com.vb4.GetMessageByIdUseCase
import com.vb4.GetMessagesByDMIdUseCase
import com.vb4.GetMessagesByGroupIdUseCase
import com.vb4.dm.DMRepository
import com.vb4.group.GroupRepository
import com.vb4.mail.imap.Imap
import com.vb4.message.MessageRepository
import com.vb4.repository.DMRepositoryImpl
import com.vb4.repository.GroupRepositoryImpl
import com.vb4.repository.MessageRepositoryImpl
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
        single<GetDestinationsUseCase> { GetDestinationsUseCase(get()) }
        single<GetMessagesByDMIdUseCase> { GetMessagesByDMIdUseCase(get(), get()) }
        single<GetMessagesByGroupIdUseCase> { GetMessagesByGroupIdUseCase(get(), get()) }
        single<GetMessageByIdUseCase> { GetMessageByIdUseCase(get()) }

        /*** Repository ***/
        single<DMRepository> { DMRepositoryImpl(get()) }
        single<GroupRepository> { GroupRepositoryImpl(get()) }
        single<MessageRepository> { MessageRepositoryImpl(Imap.Gmail("", "")) }
        single<UserRepository> { UserRepositoryImpl(get()) }

        single<Database> { DevDB }
    }

    install(Koin) { modules(module) }
}
