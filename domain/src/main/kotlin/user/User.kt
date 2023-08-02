package user

import destination.Destination

// アプリケーションの利用者を指す
data class User(
    val email: Email,
    val destinations: List<Destination>,
)

@JvmInline
value class Email(val value: String)
