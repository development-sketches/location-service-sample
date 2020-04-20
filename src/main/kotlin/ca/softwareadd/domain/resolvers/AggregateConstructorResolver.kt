package ca.softwareadd.domain.resolvers

import org.springframework.stereotype.Service
import kotlin.reflect.KClass

@Service
class AggregateConstructorResolver {

    fun findConstructor(klass: KClass<*>, idClass: KClass<*>) =
            klass.constructors.first {
                val parameters = it.parameters
                parameters.size == 2 &&
                        parameters[1].type.classifier == kotlin.Function2::class &&
                        parameters[0].type.classifier == idClass
            }

}
