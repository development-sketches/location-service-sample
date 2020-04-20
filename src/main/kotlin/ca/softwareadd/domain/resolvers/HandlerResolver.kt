package ca.softwareadd.domain.resolvers

import kotlin.reflect.KClass
import kotlin.reflect.KFunction

interface HandlerResolver {

    fun findHandler(klass: KClass<*>, type: String): KFunction<*>?

}
