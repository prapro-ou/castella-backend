package user

import destination.Destination

// アプリケーションの利用者を指す
data class User(
    val email: Email,
    val dms: List<Destination.DM>,
    val groups: List<Destination.Group>,
)

@JvmInline
value class Email(val value: String)
