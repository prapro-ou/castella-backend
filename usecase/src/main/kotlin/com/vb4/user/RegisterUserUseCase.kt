package com.vb4.user

import com.vb4.Email
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RegisterUserUseCase(
    private val userRepository: UserRepository,
    private val dispatcher: CoroutineDispatcher = Dispatchers.Default,
) {
    suspend operator fun invoke(
        email: Email,
        loginPassword: LoginPassword,
        mailPassword: MailPassword,
    ) = withContext(dispatcher) {
        userRepository.insertUser(
            User.RegisterUser.create(
                email = email,
                loginPassword = loginPassword,
                mailPassword = mailPassword,
            ),
        )
    }
}
