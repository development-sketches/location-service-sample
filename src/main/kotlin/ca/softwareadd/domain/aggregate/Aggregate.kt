package ca.softwareadd.domain.aggregate

import ca.softwareadd.domain.events.Event
import java.util.*

typealias EventConsumer = (Aggregate, Event) -> Unit

abstract class Aggregate(
        val id: UUID,
        protected val eventConsumer: EventConsumer
) {

    protected fun applyEvent(event: Event) {
        eventConsumer(this, event)
    }

}
