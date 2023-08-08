package com.vb4.plugins

import com.vb4.GetDestinationsUseCase
import com.vb4.GetMessageByIdUseCase
import com.vb4.GetMessagesByDestinationUseCase
import db.DevDB
import com.vb4.destination.DestinationRepository
import io.ktor.server.application.Application
import io.ktor.server.application.install
import com.vb4.message.MessageRepository
import org.jetbrains.exposed.sql.Database
import org.koin.dsl.module
import org.koin.ktor.plugin.Koin
import com.vb4.fake.FakeMessageRepositoryImpl
import com.vb4.repository.DestinationRepositoryImpl
import com.vb4.repository.UserRepositoryImpl
import com.vb4.user.UserRepository

fun Application.configureKoinPlugin() {
    val module = module {
        /*** UseCase ***/
        single<GetDestinationsUseCase> { GetDestinationsUseCase(get()) }
        single<GetMessagesByDestinationUseCase> { GetMessagesByDestinationUseCase(get(), get()) }
        single<GetMessageByIdUseCase> { GetMessageByIdUseCase(get()) }

        /*** Repository ***/
        single<DestinationRepository> { DestinationRepositoryImpl(get()) }
        single<MessageRepository> { FakeMessageRepositoryImpl() }
        single<UserRepository> { UserRepositoryImpl(get()) }

        single<Database> { DevDB }
    }

    install(Koin) { modules(module) }
}
