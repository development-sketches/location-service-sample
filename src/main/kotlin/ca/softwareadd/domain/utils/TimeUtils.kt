package ca.softwareadd.domain.utils

import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime

val utcZoneId: ZoneId = ZoneId.of("UTC")

fun Long.toZonedDateTime(): ZonedDateTime =
        ZonedDateTime.ofInstant(Instant.ofEpochMilli(this),
                utcZoneId)
