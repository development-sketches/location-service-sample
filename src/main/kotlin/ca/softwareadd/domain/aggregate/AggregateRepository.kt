package ca.softwareadd.domain.aggregate

import ca.softwareadd.domain.events.Event
import ca.softwareadd.domain.events.EventRepository
import ca.softwareadd.domain.events.EventSender
import ca.softwareadd.domain.routers.EventRouter
import org.springframework.stereotype.Service
import java.util.*
import kotlin.reflect.KClass

@Service
class AggregateRepository(
        private val eventRepository: EventRepository,
        private val factory: AggregateFactory,
        private val router: EventRouter,
        private val eventSender: EventSender
) {

    private fun applyEvent(aggregate: Aggregate, event: Event) {
        router.route(aggregate, event)
        eventSender.send(aggregate::class, aggregate.id, event)
    }

    fun <T : Aggregate> findById(klass: KClass<T>, id: UUID): T =
            factory.create(klass, id, eventRepository.findAllById(klass, id), this::applyEvent)

}
