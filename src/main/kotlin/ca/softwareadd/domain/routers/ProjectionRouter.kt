package ca.softwareadd.domain.routers

import ca.softwareadd.domain.resolvers.ProjectionHandlerResolver
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.kafka.support.KafkaHeaders.RECEIVED_MESSAGE_KEY
import org.springframework.messaging.Message
import org.springframework.stereotype.Service
import java.util.*
import kotlin.reflect.KClass

@Service
class ProjectionRouter(
        private val resolver: ProjectionHandlerResolver,
        private val mapper: ObjectMapper
) {

    fun handle(value: Any, message: Message<String>) {
        val id = UUID.fromString(message.headers[RECEIVED_MESSAGE_KEY] as String)
        val type = message.headers["event.type"] as String

        resolver.findHandler(value::class, type)?.apply {
            val paramType = parameters[2].type.classifier as KClass<*>
            val event = mapper.readValue(message.payload, paramType.java)
            call(value, id, event)
        }
    }

}
