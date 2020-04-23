package ca.softwareadd.domain.commands

import ca.softwareadd.country.CREATE_COUNTRY_COMMAND
import ca.softwareadd.country.Country
import ca.softwareadd.country.CountryCreatedEvent
import ca.softwareadd.country.CreateCountryCommand
import ca.softwareadd.domain.aggregate.AggregateRepository
import ca.softwareadd.domain.resolvers.CommandHandlerResolver
import ca.softwareadd.domain.routers.CommandRouter
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.given
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.kafka.support.KafkaHeaders.RECEIVED_MESSAGE_KEY
import org.springframework.messaging.support.MessageBuilder
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit.jupiter.SpringExtension
import java.util.*

@ExtendWith(SpringExtension::class)
@ContextConfiguration(classes = [
    CommandBus::class,
    CommandRouter::class,
    CommandHandlerResolver::class
])
@Import(CommandBusTest.TestConfiguration::class)
internal class CommandBusTest {

    @MockBean
    private lateinit var repository: AggregateRepository

    @Autowired
    private lateinit var mapper: ObjectMapper

    @Autowired
    private lateinit var bus: CommandBus

    @Test
    internal fun `test processing command`() {
        val id = UUID.randomUUID()

        val events = mutableListOf<Any>()
        val country = Country(id) { _, event -> events.add(event) }

        given(repository.findById(Country::class, id)).willReturn(country)

        val command = CreateCountryCommand().apply {
            alpha2code = "CA"
            alpha3code = "CAN"
            fullName = "Canada"
            numericCode = "123"
            shortName = "Canada"
        }

        val payload = mapper.writeValueAsString(command)

        val message = MessageBuilder
                .withPayload(payload)
                .setHeader(RECEIVED_MESSAGE_KEY, id.toString())
                .setHeader("command.type", CREATE_COUNTRY_COMMAND)
                .build()

        bus.handleCommand(Country::class, message)

        assertEquals(1, events.size)
        assertTrue(events.first() is CountryCreatedEvent)
    }

    @Configuration
    class TestConfiguration {

        @Bean
        fun mapper(): ObjectMapper = ObjectMapper().registerModule(KotlinModule())
    }
}
