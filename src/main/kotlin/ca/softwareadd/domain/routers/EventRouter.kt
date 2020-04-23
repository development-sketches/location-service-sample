package ca.softwareadd.domain.routers

import ca.softwareadd.domain.aggregate.Aggregate
import ca.softwareadd.domain.resolvers.EventHandlerResolver
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.stereotype.Service

@Service
class EventRouter(
        resolver: EventHandlerResolver,
        mapper: ObjectMapper
) : Router<EventHandlerResolver>(resolver, mapper) {

    fun route(aggregate: Aggregate, event: Any) {
        resolver.findHandler(aggregate::class, event::class)?.apply {
            call(aggregate, event)
        }
    }

}
