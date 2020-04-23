package ca.softwareadd.domain.resolvers

import ca.softwareadd.domain.commands.Command
import ca.softwareadd.domain.utils.toSnake
import org.springframework.stereotype.Service
import java.util.regex.Pattern
import kotlin.reflect.KClass
import kotlin.reflect.full.findAnnotation

@Service
class CommandTypeResolver {

    private val pattern = Pattern.compile("^(.+)(?=-command\$)")

    private fun String.toCommandType(): String {
        val value = toSnake('-')
        val matcher = pattern.matcher(value)
        return if (matcher.find())
            matcher.group(1)
        else
            value
    }

    fun <T : Any> commandType(klass: KClass<T>): String =
            klass.findAnnotation<Command>()?.type?.takeIf { it.isNotBlank() }
                    ?: klass.simpleName!!.toCommandType()

}
