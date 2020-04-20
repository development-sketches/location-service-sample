package ca.softwareadd.domain.routers

import ca.softwareadd.domain.aggregate.Aggregate
import ca.softwareadd.domain.resolvers.HandlerResolver
import com.fasterxml.jackson.databind.ObjectMapper
import kotlin.reflect.KClass

open class Router<T : HandlerResolver>(
        protected val resolver: T,
        private val mapper: ObjectMapper
) {

    fun route(aggregate: Aggregate, type: String, payload: String) {
        resolver.findHandler(aggregate::class, type)?.apply {
            val paramType = parameters[1].type.classifier as KClass<*>
            val command = mapper.readValue(payload, paramType.java)
            call(aggregate, command)
        }
    }

}
