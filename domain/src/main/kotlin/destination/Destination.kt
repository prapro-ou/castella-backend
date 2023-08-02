package destination

import user.Email

sealed interface Destination {
    data class DM(
        val id: DestinationId,
        val first: Email,
        val second: Email,
    ) : Destination
}

@JvmInline
value class DestinationId(val value: String)
