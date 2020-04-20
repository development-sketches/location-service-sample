package ca.softwareadd.domain.resolvers

import ca.softwareadd.domain.commands.CommandHandler
import org.springframework.stereotype.Service
import kotlin.reflect.KClass
import kotlin.reflect.full.declaredMemberFunctions
import kotlin.reflect.full.findAnnotation

@Service
class CommandHandlerResolver : HandlerResolver {

    override fun findHandler(klass: KClass<*>, type: String) =
            klass.declaredMemberFunctions.asSequence()
                    .firstOrNull {
                        it.parameters.size == 2 &&
                                it.parameters[0].type.classifier == klass &&
                                it.findAnnotation<CommandHandler>()?.type == type
                    }

}
