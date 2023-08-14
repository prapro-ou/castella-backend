package db.table

import com.vb4.Email
import com.vb4.dm.DM
import com.vb4.group.Group
import com.vb4.user.MailPassword
import com.vb4.user.TempUser
import com.vb4.user.User
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.Table

object UsersTable : Table("users") {
    val email = varchar("email", 256)
    val loginPassword = varchar("login_password", 256)
    val mailPassword = varchar("mail_password", 256)

    override val primaryKey: PrimaryKey = PrimaryKey(email)
}

internal fun ResultRow.toUser() = User.reconstruct(
    email = Email(this[UsersTable.email]),
)

internal fun ResultRow.toAuthUser() = TempUser.AuthUser.reconstruct(
    email = Email(this[UsersTable.email]),
    password = MailPassword(this[UsersTable.mailPassword]),
)
