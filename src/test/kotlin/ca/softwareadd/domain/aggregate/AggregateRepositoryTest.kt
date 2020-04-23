package ca.softwareadd.domain.aggregate

import ca.softwareadd.country.Country
import ca.softwareadd.country.CountryCreatedEvent
import ca.softwareadd.country.CreateCountryCommand
import ca.softwareadd.domain.events.EventEntity
import ca.softwareadd.domain.events.EventSender
import ca.softwareadd.domain.events.EventStore
import ca.softwareadd.domain.resolvers.AggregateConstructorResolver
import ca.softwareadd.domain.resolvers.EventHandlerResolver
import ca.softwareadd.domain.resolvers.EventTypeResolver
import ca.softwareadd.domain.routers.EventRouter
import ca.softwareadd.utils.anyK
import ca.softwareadd.utils.eqK
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit.jupiter.SpringExtension
import java.util.*

@ExtendWith(SpringExtension::class)
@ContextConfiguration(classes = [
    AggregateFactory::class,
    AggregateConstructorResolver::class,
    EventHandlerResolver::class,
    EventTypeResolver::class,
    EventRouter::class,
    AggregateRepository::class
])
@Import(AggregateRepositoryTest.TestConfiguration::class)
internal class AggregateRepositoryTest {

    @MockBean
    private lateinit var eventStore: EventStore

    @MockBean
    private lateinit var eventSender: EventSender

    @Autowired
    private lateinit var mapper: ObjectMapper

    @Autowired
    private lateinit var repository: AggregateRepository

    @Test
    internal fun `reconstruct an aggregate from history by id`() {
        val id = UUID.randomUUID()

        val event = CountryCreatedEvent("CA", "CAN", "Canada", "123", "Canada")
        val json = mapper.writeValueAsString(event)
        val history = listOf(
                EventEntity().apply {
                    this.id = id
                    type = "country-created"
                    this.json = json
                }
        )

        given(eventStore.findAllById(Country::class, id)).willReturn(history)

        val country = repository.findById(Country::class, id)

        verify(eventStore, times(1))
                .findAllById(eqK(Country::class), eqK(id))

        assertEquals(id, country.id)
        assertEquals(event.alpha2code, country.alpha2code)
        assertEquals(event.alpha3code, country.alpha3code)
        assertEquals(event.fullName, country.fullName)
        assertEquals(event.numericCode, country.numericCode)
        assertEquals(event.shortName, country.shortName)
    }

    @Test
    internal fun `return a new aggregate if no history`() {
        val id = UUID.randomUUID()

        given(eventStore.findAllById(Country::class, id)).willReturn(emptyList())

        val country = repository.findById(Country::class, id)

        verify(eventStore, times(1))
                .findAllById(eqK(Country::class), eqK(id))

        assertEquals(id, country.id)
    }

    @Test
    internal fun `send an event received from the aggregate`() {
        val id = UUID.randomUUID()

        given(eventStore.findAllById(Country::class, id)).willReturn(emptyList())

        val country = repository.findById(Country::class, id)

        val command = CreateCountryCommand().apply {
            alpha2code = "CA"
            alpha3code = "CAN"
            fullName = "Canada"
            numericCode = "123"
            shortName = "Canada"
        }
        country.createCountry(command)

        verify(eventSender, times(1)).send(eqK(country), anyK())
    }

    @Configuration
    class TestConfiguration {

        @Bean
        fun mapper(): ObjectMapper = ObjectMapper().registerModule(KotlinModule())
    }
}
