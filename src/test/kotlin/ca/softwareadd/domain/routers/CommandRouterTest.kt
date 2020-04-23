package ca.softwareadd.domain.routers

import ca.softwareadd.country.Country
import ca.softwareadd.country.CountryCreatedEvent
import ca.softwareadd.country.CreateCountryCommand
import ca.softwareadd.domain.resolvers.CommandHandlerResolver
import ca.softwareadd.domain.resolvers.CommandTypeResolver
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit.jupiter.SpringExtension
import java.util.*

private const val CREATE_COUNTRY_COMMAND = "create-country"

@ExtendWith(SpringExtension::class)
@ContextConfiguration(classes = [
    CommandRouter::class,
    CommandHandlerResolver::class,
    CommandTypeResolver::class
])
@Import(CommandRouterTest.TestConfiguration::class)
internal class CommandRouterTest {

    @Autowired
    private lateinit var router: CommandRouter

    @Autowired
    private lateinit var mapper: ObjectMapper

    @Test
    internal fun `test handler invocation`() {
        val events = mutableListOf<Any>()

        val country = Country(UUID.randomUUID()) { _, event -> events.add(event) }

        val command = CreateCountryCommand().apply {
            alpha2code = "CA"
            alpha3code = "CAN"
            fullName = "Canada"
            numericCode = "123"
            shortName = "Canada"
        }

        val json = mapper.writeValueAsString(command)

        router.route(country, CREATE_COUNTRY_COMMAND, json)

        assertEquals(1, events.size)
        val event = events[0] as CountryCreatedEvent
        assertEquals(command.alpha2code, event.alpha2code)
        assertEquals(command.alpha3code, event.alpha3code)
        assertEquals(command.fullName, event.fullName)
        assertEquals(command.numericCode, event.numericCode)
        assertEquals(command.shortName, event.shortName)
    }

    @Configuration
    class TestConfiguration {

        @Bean
        fun mapper(): ObjectMapper = ObjectMapper()
    }
}
