package com.vb4.plugins

import com.vb4.dm.GetDMMessageByIdUseCase
import com.vb4.user.GetUserDestinationsUseCase
import com.vb4.dm.GetDMMessagesByDMIdUseCase
import com.vb4.group.GetGroupMessagesByGroupIdUseCase
import com.vb4.dm.DMRepository
import com.vb4.group.GroupRepository
import com.vb4.mail.imap.Imap
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
        single<GetUserDestinationsUseCase> { GetUserDestinationsUseCase(get(), get()) }
        single<GetDMMessagesByDMIdUseCase> { GetDMMessagesByDMIdUseCase(get()) }
        single<GetGroupMessagesByGroupIdUseCase> { GetGroupMessagesByGroupIdUseCase(get()) }
        single<GetDMMessageByIdUseCase> { GetDMMessageByIdUseCase(get()) }

        /*** Repository ***/
        single<DMRepository> { DMRepositoryImpl(get(), get()) }
        single<GroupRepository> { GroupRepositoryImpl(get()) }
        single<UserRepository> { UserRepositoryImpl(get()) }
        single<Imap> { Imap.Gmail("inputUserEmail", "") }

        single<Database> { DevDB }
    }

    install(Koin) { modules(module) }
}
