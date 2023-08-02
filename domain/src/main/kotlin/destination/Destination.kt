package destination

import user.User

sealed interface Destination {
    data class DM(
        val id: DestinationId,
        val user: User,
        val to: User,
    ) : Destination
}

@JvmInline
value class DestinationId(val value: String)
