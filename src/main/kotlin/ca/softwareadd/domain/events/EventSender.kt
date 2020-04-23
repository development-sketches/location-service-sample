package ca.softwareadd.domain.events

import ca.softwareadd.domain.aggregate.Aggregate
import ca.softwareadd.domain.resolvers.EventTypeResolver
import ca.softwareadd.domain.routers.EventRouter
import org.springframework.context.annotation.Bean
import org.springframework.kafka.support.KafkaHeaders.MESSAGE_KEY
import org.springframework.messaging.Message
import org.springframework.messaging.support.MessageBuilder
import org.springframework.stereotype.Service
import reactor.core.publisher.EmitterProcessor
import reactor.core.publisher.Flux
import java.util.function.Supplier
import kotlin.reflect.KClass

private const val SEND_TO_DESTINATION = "spring.cloud.stream.sendto.destination"

@Service
class EventSender(
        private val router: EventRouter,
        private val eventTypeResolver: EventTypeResolver
) {

    private val emitterProcessor = EmitterProcessor.create<Message<Any>>()
    private val sink = emitterProcessor.sink()

    private fun <T : Aggregate> bindingName(klass: KClass<T>): String =
            klass.simpleName!!.decapitalize() + "Events-out-0"

    fun <T : Aggregate> send(aggregate: T, event: Any) {
        val message = MessageBuilder
                .withPayload(event)
                .setHeader(MESSAGE_KEY, aggregate.id.toString())
                .setHeader("event.type", eventTypeResolver.eventType(event::class))
                .setHeader(SEND_TO_DESTINATION, bindingName(aggregate::class))
                .build()
        sink.next(message)
        router.route(aggregate, event)
    }

    @Bean
    fun events(): Supplier<Flux<Message<Any>>> = Supplier {
        emitterProcessor
    }

}
