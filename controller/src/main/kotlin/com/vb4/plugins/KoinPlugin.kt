package com.vb4.plugins

import GetDestinationsUseCase
import GetMessageByIdUseCase
import GetMessagesByDestinationUseCase
import destination.DestinationRepository
import io.ktor.server.application.Application
import io.ktor.server.application.install
import message.MessageRepository
import org.koin.dsl.module
import org.koin.ktor.plugin.Koin
import repository.fake.FakeDestinationRepositoryImpl
import repository.fake.FakeMessageRepositoryImpl
import repository.fake.FakeUserRepositoryImpl
import user.UserRepository

fun Application.configureKoinPlugin() {
    val module = module {
        /*** UseCase ***/
        single<GetDestinationsUseCase> { GetDestinationsUseCase(get()) }
        single<GetMessagesByDestinationUseCase> { GetMessagesByDestinationUseCase(get(), get()) }
        single<GetMessageByIdUseCase> { GetMessageByIdUseCase(get()) }

        /*** Repository ***/
        single<DestinationRepository> { FakeDestinationRepositoryImpl() }
        single<MessageRepository> { FakeMessageRepositoryImpl() }
        single<UserRepository> { FakeUserRepositoryImpl() }
    }

    install(Koin) { modules(module) }
}
