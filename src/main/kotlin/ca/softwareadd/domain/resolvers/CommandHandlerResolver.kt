package ca.softwareadd.domain.resolvers

import ca.softwareadd.domain.commands.CommandHandler
import org.springframework.stereotype.Service
import kotlin.reflect.KClass
import kotlin.reflect.full.declaredMemberFunctions
import kotlin.reflect.full.findAnnotation

@Service
class CommandHandlerResolver(
        private val commandTypeResolver: CommandTypeResolver
) : HandlerResolver {

    override fun findHandler(klass: KClass<*>, type: String) =
            klass.declaredMemberFunctions.asSequence()
                    .firstOrNull {
                        it.findAnnotation<CommandHandler>() != null &&
                                it.parameters.size == 2 &&
                                it.parameters[0].type.classifier == klass &&
                                commandTypeResolver.commandType(it.parameters[1].type.classifier as KClass<*>) == type
                    }

}
