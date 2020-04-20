package ca.softwareadd.domain.commands

import ca.softwareadd.domain.aggregate.Aggregate
import ca.softwareadd.domain.aggregate.AggregateRepository
import ca.softwareadd.domain.events.EventEntity
import ca.softwareadd.domain.events.EventRepository
import ca.softwareadd.domain.routers.CommandRouter
import ca.softwareadd.domain.utils.toZonedDateTime
import org.springframework.kafka.support.KafkaHeaders.RECEIVED_MESSAGE_KEY
import org.springframework.kafka.support.KafkaHeaders.RECEIVED_TIMESTAMP
import org.springframework.messaging.Message
import org.springframework.stereotype.Service
import java.util.*
import kotlin.reflect.KClass

@Service
class CommandBus(
        private val router: CommandRouter,
        private val eventRepository: EventRepository,
        private val aggregateRepository: AggregateRepository
) {

    fun <T : Aggregate> handleCommand(klass: KClass<T>, message: Message<String>) {
        val id = UUID.fromString(message.headers[RECEIVED_MESSAGE_KEY] as String)
        val type = message.headers["command.type"] as String
        val aggregate = aggregateRepository.findById(klass, id)
        router.route(aggregate, type, message.payload)
    }

    fun <T : Aggregate> handleEvent(klass: KClass<T>, message: Message<String>) {
        val id = UUID.fromString(message.headers[RECEIVED_MESSAGE_KEY] as String)
        val timestamp = (message.headers[RECEIVED_TIMESTAMP] as Long).toZonedDateTime()
        val type = message.headers["event.type"] as String

        val entity = EventEntity().apply {
            this.id = id
            this.version = eventRepository.countAllById(klass, id) + 1
            this.type = type
            this.timestamp = timestamp
            this.json = message.payload
        }
        eventRepository.save(klass, entity)
    }

}
