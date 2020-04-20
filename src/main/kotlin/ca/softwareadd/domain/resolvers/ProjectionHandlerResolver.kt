package ca.softwareadd.domain.resolvers

import ca.softwareadd.domain.events.EventHandler
import org.springframework.stereotype.Service
import java.util.*
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.full.declaredMemberFunctions
import kotlin.reflect.full.findAnnotation

@Service
class ProjectionHandlerResolver : HandlerResolver {

    override fun findHandler(klass: KClass<*>, type: String): KFunction<*>? =
            klass.declaredMemberFunctions.asSequence()
                    .firstOrNull {
                        it.parameters.size == 3 &&
                                it.parameters[0].type.classifier == klass &&
                                it.parameters[1].type.classifier == UUID::class &&
                                it.findAnnotation<EventHandler>()?.type == type
                    }
}
