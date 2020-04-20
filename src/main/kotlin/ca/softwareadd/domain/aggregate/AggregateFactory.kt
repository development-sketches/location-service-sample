package ca.softwareadd.domain.aggregate

import ca.softwareadd.domain.events.EventEntity
import ca.softwareadd.domain.resolvers.AggregateConstructorResolver
import ca.softwareadd.domain.routers.EventRouter
import org.springframework.stereotype.Service
import java.util.*
import kotlin.reflect.KClass

@Service
class AggregateFactory(
        private val constructorResolver: AggregateConstructorResolver,
        private val router: EventRouter
) {

    @Suppress("UNCHECKED_CAST")
    fun <T : Aggregate> create(klass: KClass<T>, id: UUID, consumer: EventConsumer): T =
            constructorResolver
                    .findConstructor(klass, id::class)
                    .call(id, consumer) as T

    fun <T : Aggregate> create(klass: KClass<T>, id: UUID, history: List<EventEntity>, consumer: EventConsumer): T =
            history.fold(create(klass, id, consumer)) { aggregate, event ->
                router.route(aggregate, event.type, event.json)
                aggregate
            }

}
