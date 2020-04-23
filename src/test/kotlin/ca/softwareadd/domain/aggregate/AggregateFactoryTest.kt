package ca.softwareadd.domain.aggregate

import ca.softwareadd.country.Country
import ca.softwareadd.country.CountryCreatedEvent
import ca.softwareadd.domain.events.EventEntity
import ca.softwareadd.domain.resolvers.AggregateConstructorResolver
import ca.softwareadd.domain.resolvers.EventHandlerResolver
import ca.softwareadd.domain.resolvers.EventTypeResolver
import ca.softwareadd.domain.routers.EventRouter
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
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

@ExtendWith(SpringExtension::class)
@ContextConfiguration(classes = [
    AggregateFactory::class,
    EventRouter::class,
    AggregateConstructorResolver::class,
    EventHandlerResolver::class,
    EventTypeResolver::class
])
@Import(AggregateFactoryTest.TestConfiguration::class)
internal class AggregateFactoryTest {

    @Autowired
    private lateinit var factory: AggregateFactory

    @Autowired
    private lateinit var mapper: ObjectMapper

    @Test
    internal fun `test creating a new aggregate`() {
        val id = UUID.randomUUID()
        val country = factory.create(Country::class, id) { _, _ -> }
        assertEquals(id, country.id)
    }

    @Test
    internal fun `test restoring an aggregate`() {
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
        val country = factory.create(Country::class, id, history) { _, _ -> }
        assertEquals(id, country.id)
        assertEquals(event.alpha2code, country.alpha2code)
        assertEquals(event.alpha3code, country.alpha3code)
        assertEquals(event.fullName, country.fullName)
        assertEquals(event.numericCode, country.numericCode)
        assertEquals(event.shortName, country.shortName)
    }

    @Configuration
    class TestConfiguration {

        @Bean
        fun mapper(): ObjectMapper = ObjectMapper().registerModule(KotlinModule())
    }
}
