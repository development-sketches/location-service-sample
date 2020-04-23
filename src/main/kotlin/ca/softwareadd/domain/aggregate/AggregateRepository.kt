package ca.softwareadd.domain.aggregate

import ca.softwareadd.domain.events.EventSender
import ca.softwareadd.domain.events.EventStore
import org.springframework.stereotype.Service
import java.util.*
import kotlin.reflect.KClass

@Service
class AggregateRepository(
        private val eventStore: EventStore,
        private val factory: AggregateFactory,
        private val eventSender: EventSender
) {

    private fun applyEvent(aggregate: Aggregate, event: Any) {
        eventSender.send(aggregate, event)
    }

    fun <T : Aggregate> findById(klass: KClass<T>, id: UUID): T =
            factory.create(klass, id, eventStore.findAllById(klass, id), this::applyEvent)

}
