package ca.softwareadd.domain.events

import java.time.ZonedDateTime
import java.util.*

class EventEntity {

    lateinit var id: UUID

    var version: Int = 0

    lateinit var type: String

    lateinit var timestamp: ZonedDateTime

    var userId: UUID? = null

    lateinit var json: String

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is EventEntity) return false

        return id == other.id && version == other.version
    }

    override fun hashCode(): Int = Objects.hash(id, version)

    override fun toString(): String =
            "EventEntity(id=$id, version=$version, type=$type, timestamp=$timestamp, userId=$userId, json=$json)"

}
