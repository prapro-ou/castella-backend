package destination

import user.User

sealed interface Destination {

    val id: DestinationId

    data class DM(
        override val id: DestinationId,
        val user: User,
        val to: User,
    ) : Destination

    data class Group(
        override val id: DestinationId,
        val user: User,
        val to: List<User>,
    ) : Destination

    companion object {
        fun List<Destination>.divide(): Pair<List<DM>, List<Group>> =
            this.fold(listOf<DM>() to listOf<Group>()) { acc, destination ->
                when (destination) {
                    is DM -> (acc.first + destination) to acc.second
                    is Group -> acc.first to (acc.second + destination)
                }
            }
    }
}

@JvmInline
value class DestinationId(val value: String)
