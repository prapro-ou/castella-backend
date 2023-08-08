package com.vb4.destination

import com.vb4.avatar.Avatar

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
}

@JvmInline
value class DestinationId(val value: String)

@JvmInline
value class DestinationName(val value: String)
