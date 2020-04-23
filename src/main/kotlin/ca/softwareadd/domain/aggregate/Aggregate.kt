package ca.softwareadd.domain.aggregate

import java.util.*

typealias EventConsumer = (Aggregate, Any) -> Unit

abstract class Aggregate(
        val id: UUID,
        protected val eventConsumer: EventConsumer
) {

    protected fun applyEvent(event: Any) {
        eventConsumer(this, event)
    }

}
