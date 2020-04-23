package ca.softwareadd.domain.commands

import ca.softwareadd.domain.aggregate.Aggregate
import ca.softwareadd.domain.aggregate.AggregateRepository
import ca.softwareadd.domain.routers.CommandRouter
import org.springframework.kafka.support.KafkaHeaders.RECEIVED_MESSAGE_KEY
import org.springframework.messaging.Message
import org.springframework.stereotype.Service
import java.util.*
import kotlin.reflect.KClass

@Service
class CommandBus(
        private val router: CommandRouter,
        private val aggregateRepository: AggregateRepository
) {

    fun <T : Aggregate> handleCommand(klass: KClass<T>, message: Message<String>) {
        val id = UUID.fromString(message.headers[RECEIVED_MESSAGE_KEY] as String)
        val type = message.headers["command.type"] as String
        val aggregate = aggregateRepository.findById(klass, id)
        router.route(aggregate, type, message.payload)
    }

}
