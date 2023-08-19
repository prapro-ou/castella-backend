package com.vb4.repository

import com.vb4.DomainException
import com.vb4.Email
import com.vb4.result.ApiResult
import com.vb4.runCatchWithContext
import com.vb4.suspendTransaction
import com.vb4.user.LoginPassword
import com.vb4.user.User
import com.vb4.user.UserRepository
import db.table.UsersTable
import db.table.toAuthUser
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import java.security.MessageDigest

class UserRepositoryImpl(
    private val database: Database,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
) : UserRepository {
    override suspend fun getUser(email: Email): ApiResult<User.AuthUser, DomainException> =
        runCatchWithContext(dispatcher) {
            suspendTransaction(database) {
                UsersTable
                    .select { UsersTable.email eq email.value }
                    .first()
                    .toAuthUser()
            }
        }

    override suspend fun authUser(
        user: User.BeforeAuthUser,
    ): ApiResult<User.AuthUser, DomainException> =
        runCatchWithContext(dispatcher) {
            val dbUser = suspendTransaction(database) {
                UsersTable
                    .select { UsersTable.email eq user.email.value }
                    .firstOrNull()
            }

            if (dbUser == null || dbUser[UsersTable.loginPassword] != user.password.toHash()) {
                throw DomainException.AuthException("Auth failed.")
            }
            dbUser.toAuthUser()
        }

    override suspend fun insertUser(
        user: User.RegisterUser,
    ): ApiResult<Unit, DomainException> =
        runCatchWithContext(dispatcher) {
            suspendTransaction(database) {
                UsersTable.insert {
                    it[email] = user.email.value
                    it[loginPassword] = user.loginPassword.toHash()
                    it[mailPassword] = user.mailPassword.value
                }
            }
        }
}

fun LoginPassword.toHash(): String = MessageDigest.getInstance("SHA-256")
    .digest((this.value + SALT).toByteArray())
    .joinToString { "%02x".format(it) }

private const val SALT = "2Ktlj2hF"
