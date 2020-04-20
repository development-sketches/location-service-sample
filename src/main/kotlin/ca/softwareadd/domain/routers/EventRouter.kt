package ca.softwareadd.domain.routers

import ca.softwareadd.domain.aggregate.Aggregate
import ca.softwareadd.domain.events.Event
import ca.softwareadd.domain.resolvers.EventHandlerResolver
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.stereotype.Service

@Service
class EventRouter(
        resolver: EventHandlerResolver,
        mapper: ObjectMapper
) : Router<EventHandlerResolver>(resolver, mapper) {

    fun route(aggregate: Aggregate, event: Event) {
        resolver.findHandler(aggregate::class, event::class, event.type)?.apply {
            call(aggregate, event)
        }
    }

}
