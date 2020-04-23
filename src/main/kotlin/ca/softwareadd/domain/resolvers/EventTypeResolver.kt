package ca.softwareadd.domain.resolvers

import ca.softwareadd.domain.events.Event
import ca.softwareadd.domain.utils.toSnake
import org.springframework.stereotype.Service
import java.util.regex.Pattern
import kotlin.reflect.KClass
import kotlin.reflect.full.findAnnotation

@Service
class EventTypeResolver {

    private val pattern = Pattern.compile("^(.+)(?=-event\$)")

    private fun String.toEventType(): String {
        val value = toSnake('-')
        val matcher = pattern.matcher(value)
        return if (matcher.find())
            matcher.group(1)
        else
            value
    }

    fun <T : Any> eventType(klass: KClass<T>): String =
            klass.findAnnotation<Event>()?.type?.takeIf { it.isNotBlank() }
                    ?: klass.simpleName!!.toEventType()

}
