package ca.softwareadd.domain.events

import ca.softwareadd.domain.aggregate.Aggregate
import org.springframework.context.annotation.Bean
import org.springframework.kafka.support.KafkaHeaders.MESSAGE_KEY
import org.springframework.messaging.Message
import org.springframework.messaging.support.MessageBuilder
import org.springframework.stereotype.Service
import reactor.core.publisher.EmitterProcessor
import reactor.core.publisher.Flux
import java.util.*
import java.util.function.Supplier
import kotlin.reflect.KClass

private const val SEND_TO_DESTINATION = "spring.cloud.stream.sendto.destination"

@Service
class EventSender {

    private val emitterProcessor = EmitterProcessor.create<Message<Event>>()
    private val sink = emitterProcessor.sink()

    private fun <T : Aggregate> bindingName(klass: KClass<T>): String =
            klass.simpleName!!.decapitalize() + "Events-out-0"

    fun <T : Aggregate> send(klass: KClass<T>, id: UUID, event: Event) {
        val message = MessageBuilder
                .withPayload(event)
                .setHeader(MESSAGE_KEY, id.toString())
                .setHeader("event.type", event.type)
                .setHeader(SEND_TO_DESTINATION, bindingName(klass))
                .build()
        sink.next(message)
    }

    @Bean
    fun events(): Supplier<Flux<Message<Event>>> = Supplier {
        emitterProcessor
    }

}
