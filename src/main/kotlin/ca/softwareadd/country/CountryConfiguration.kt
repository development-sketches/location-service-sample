package ca.softwareadd.country

import ca.softwareadd.domain.commands.CommandBus
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.messaging.Message
import java.util.function.Consumer

@Configuration
class CountryConfiguration(
        private val commandBus: CommandBus,
        private val handler: CountryProjectionHandler
) {

    @Bean
    fun countryCommands(): Consumer<Message<String>> = Consumer { message ->
        commandBus.handleCommand(Country::class, message)
    }

    @Bean
    fun countryEventStore(): Consumer<Message<String>> = Consumer { message ->
        commandBus.handleEvent(Country::class, message)
    }

    @Bean
    fun countryProjection(): Consumer<Message<String>> = Consumer { message ->
        handler.handle(message)
    }
}
