package destination

import avatar.Avatar

sealed interface Destination {

    val id: DestinationId
    val name: DestinationName

    data class DM(
        override val id: DestinationId,
        override val name: DestinationName,
        val to: Avatar,
    ) : Destination

    data class Group(
        override val id: DestinationId,
        override val name: DestinationName,
        val to: List<Avatar>,
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

@JvmInline
value class DestinationName(val value: String)
