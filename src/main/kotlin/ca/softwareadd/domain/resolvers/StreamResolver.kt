package ca.softwareadd.domain.resolvers

import ca.softwareadd.domain.aggregate.Aggregate
import ca.softwareadd.domain.utils.toSnake
import org.springframework.stereotype.Service
import kotlin.reflect.KClass

@Service
class StreamResolver {

    fun <T : Aggregate> streamForClass(klass: KClass<T>): String =
            klass.simpleName!!.toSnake()

}
