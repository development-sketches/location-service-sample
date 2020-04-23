package ca.softwareadd.domain.events

import ca.softwareadd.domain.aggregate.Aggregate
import ca.softwareadd.domain.utils.toZonedDateTime
import org.springframework.kafka.support.KafkaHeaders.RECEIVED_MESSAGE_KEY
import org.springframework.kafka.support.KafkaHeaders.RECEIVED_TIMESTAMP
import org.springframework.messaging.Message
import org.springframework.stereotype.Service
import java.util.*
import kotlin.reflect.KClass

@Service
class EventStoreConsumer(
        private val eventStore: EventStore
) {

    fun <T : Aggregate> handleEvent(klass: KClass<T>, message: Message<String>) {
        val id = UUID.fromString(message.headers[RECEIVED_MESSAGE_KEY] as String)
        val timestamp = (message.headers[RECEIVED_TIMESTAMP] as Long).toZonedDateTime()
        val type = message.headers["event.type"] as String

        val entity = EventEntity().apply {
            this.id = id
            this.version = eventStore.countAllById(klass, id) + 1
            this.type = type
            this.timestamp = timestamp
            this.json = message.payload
        }
        eventStore.save(klass, entity)
    }

}
