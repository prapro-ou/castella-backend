package com.vb4.repository

import com.vb4.DomainException
import com.vb4.Email
import com.vb4.result.ApiResult
import com.vb4.runCatchWithContext
import com.vb4.suspendTransaction
import com.vb4.user.TempUser
import com.vb4.user.User
import com.vb4.user.UserRepository
import db.table.UsersTable
import db.table.toAuthUser
import db.table.toUser
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.select

class UserRepositoryImpl(
    private val database: Database,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
) : UserRepository {
    override suspend fun getUser(email: Email): ApiResult<User, DomainException> =
        runCatchWithContext(dispatcher) {
            suspendTransaction(database) {
                UsersTable
                    .select { UsersTable.email eq email.value }
                    .first()
                    .toUser()
            }
        }

    override suspend fun authUser(
        user: TempUser.BeforeAuthUser,
    ): ApiResult<TempUser.AuthUser, DomainException> =
        runCatchWithContext(dispatcher) {
            val dbUser = suspendTransaction(database) {
                UsersTable
                    .select { UsersTable.email eq user.email.value }
                    .first()
            }
            if (dbUser[UsersTable.loginPassword] == user.password.value)
                dbUser.toAuthUser()
            else throw DomainException.AuthException("Auth failed.")
        }
}
