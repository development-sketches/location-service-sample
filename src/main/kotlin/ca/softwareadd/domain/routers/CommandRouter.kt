package ca.softwareadd.domain.routers

import ca.softwareadd.domain.resolvers.CommandHandlerResolver
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.stereotype.Service

@Service
class CommandRouter(
        resolver: CommandHandlerResolver,
        mapper: ObjectMapper
) : Router<CommandHandlerResolver>(resolver, mapper)
