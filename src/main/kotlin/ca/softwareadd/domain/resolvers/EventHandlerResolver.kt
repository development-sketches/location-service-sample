package ca.softwareadd.domain.resolvers

import ca.softwareadd.domain.events.EventHandler
import org.springframework.stereotype.Service
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.full.declaredMemberFunctions
import kotlin.reflect.full.findAnnotation

@Service
class EventHandlerResolver(
        private val eventTypeResolver: EventTypeResolver
) : HandlerResolver {

    fun findHandler(klass: KClass<*>, eventClass: KClass<*>): KFunction<*>? =
            klass.declaredMemberFunctions.asSequence()
                    .firstOrNull {
                        it.findAnnotation<EventHandler>() != null &&
                                it.parameters.size == 2 &&
                                it.parameters[0].type.classifier == klass &&
                                it.parameters[1].type.classifier == eventClass
                    }

    override fun findHandler(klass: KClass<*>, type: String) =
            klass.declaredMemberFunctions.asSequence()
                    .firstOrNull {
                        it.findAnnotation<EventHandler>() != null &&
                                it.parameters.size == 2 &&
                                it.parameters[0].type.classifier == klass &&
                                eventTypeResolver.eventType(it.parameters[1].type.classifier as KClass<*>) == type
                    }

}
