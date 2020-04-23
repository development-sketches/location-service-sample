package ca.softwareadd.domain.routers

import ca.softwareadd.country.Country
import ca.softwareadd.country.CountryCreatedEvent
import ca.softwareadd.domain.resolvers.EventHandlerResolver
import ca.softwareadd.domain.resolvers.EventTypeResolver
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

private const val COUNTRY_CREATED_EVENT = "country-created"

@ExtendWith(SpringExtension::class)
@ContextConfiguration(classes = [
    EventRouter::class,
    EventHandlerResolver::class,
    EventTypeResolver::class
])
@Import(EventRouterTest.TestConfiguration::class)
internal class EventRouterTest {

    @Autowired
    private lateinit var router: EventRouter

    @Autowired
    private lateinit var mapper: ObjectMapper

    @Test
    internal fun `test handler invocation`() {
        val event = CountryCreatedEvent("CA", "CAN", "Canada", "123", "Canada")
        val json = mapper.writeValueAsString(event)
        val country = Country(UUID.randomUUID()) { _, _ -> }
        router.route(country, COUNTRY_CREATED_EVENT, json)

        assertEquals(event.alpha2code, country.alpha2code)
        assertEquals(event.alpha3code, country.alpha3code)
        assertEquals(event.fullName, country.fullName)
        assertEquals(event.numericCode, country.numericCode)
        assertEquals(event.shortName, country.shortName)
    }

    @Test
    internal fun `test handler invocation with event`() {
        val event = CountryCreatedEvent("CA", "CAN", "Canada", "123", "Canada")
        val country = Country(UUID.randomUUID()) { _, _ -> }
        router.route(country, event)

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
