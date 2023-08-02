package com.vb4.plugins

import GetDestinationsUseCase
import GetMessagesByDestinationUseCase
import GetMessagesWithRepliesUseCase
import destination.DestinationRepository
import io.ktor.server.application.*
import io.ktor.server.application.Application
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
        single<GetMessagesWithRepliesUseCase> { GetMessagesWithRepliesUseCase(get()) }

        /*** Repository ***/
        single<DestinationRepository> { FakeDestinationRepositoryImpl() }
        single<MessageRepository> { FakeMessageRepositoryImpl() }
        single<UserRepository> { FakeUserRepositoryImpl() }
    }

    install(Koin) { module }
}
