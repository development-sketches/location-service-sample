package ca.softwareadd.domain.commands

import ca.softwareadd.country.*
import ca.softwareadd.domain.aggregate.AggregateRepository
import ca.softwareadd.domain.events.Event
import ca.softwareadd.domain.events.EventEntity
import ca.softwareadd.domain.events.EventRepository
import ca.softwareadd.domain.resolvers.CommandHandlerResolver
import ca.softwareadd.domain.routers.CommandRouter
import ca.softwareadd.utils.eqK
import ca.softwareadd.utils.uninitialized
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.ArgumentCaptor
import org.mockito.BDDMockito.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.kafka.support.KafkaHeaders.RECEIVED_MESSAGE_KEY
import org.springframework.kafka.support.KafkaHeaders.RECEIVED_TIMESTAMP
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

    @MockBean
    private lateinit var eventRepository: EventRepository

    @Autowired
    private lateinit var mapper: ObjectMapper

    @Autowired
    private lateinit var bus: CommandBus

    @Test
    internal fun `test processing command`() {
        val id = UUID.randomUUID()

        val events = mutableListOf<Event>()
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

    @Test
    internal fun `test processing event`() {
        val id = UUID.randomUUID()
        given(eventRepository.countAllById(Country::class, id)).willReturn(0)

        val message = MessageBuilder
                .withPayload("{}")
                .setHeader(RECEIVED_MESSAGE_KEY, id.toString())
                .setHeader(RECEIVED_TIMESTAMP, 0L)
                .setHeader("event.type", COUNTRY_CREATED_EVENT)
                .build()

        bus.handleEvent(Country::class, message)

        verify(eventRepository, times(1))
                .countAllById(eqK(Country::class), eqK(id))

        val captor = ArgumentCaptor.forClass(EventEntity::class.java)
        verify(eventRepository, times(1))
                .save(eqK(Country::class), captor.capture() ?: uninitialized())

        val entity = captor.value
        assertEquals(id, entity.id)
        assertEquals(COUNTRY_CREATED_EVENT, entity.type)
        assertEquals(1, entity.version)
    }

    //
//    @Test
//    internal fun `test sending a command`() {
//        val command = CreateCountryCommand().apply {
//            alpha2code = "CA"
//            alpha3code = "CAN"
//            fullName = "Canada"
//            numericCode = "123"
//            shortName = "Canada"
//        }
//
//        val id = UUID.randomUUID()
//
//        bus.send(id, command)
//
//        val captor = ArgumentCaptor.forClass(Message::class.java)
//
//        verify(channel, times(1)).send(captor.capture() ?: uninitialized())
//
//        val message = captor.value
//
//        assertEquals(id.toString(), message.headers[MESSAGE_KEY])
//        assertEquals(command.type, message.headers[COMMAND_TYPE_HEADER])
//
//        val payload = message.payload as CreateCountryCommand
//        assertEquals(command.alpha2code, payload.alpha2code)
//    }

    @Configuration
    class TestConfiguration {

        @Bean
        fun mapper(): ObjectMapper = ObjectMapper().registerModule(KotlinModule())
    }
}
