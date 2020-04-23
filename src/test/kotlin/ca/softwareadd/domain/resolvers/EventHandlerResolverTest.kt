package ca.softwareadd.domain.resolvers

import ca.softwareadd.country.Country
import ca.softwareadd.country.CountryCreatedEvent
import ca.softwareadd.domain.events.EventEntity
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit.jupiter.SpringExtension
import java.util.*

private const val COUNTRY_CREATED_EVENT = "country-created"

@ExtendWith(SpringExtension::class)
@ContextConfiguration(classes = [
    EventHandlerResolver::class,
    EventTypeResolver::class
])
internal class EventHandlerResolverTest {

    @Autowired
    private lateinit var resolver: EventHandlerResolver

    @Test
    internal fun `resolver should return method annotated with EventHandler`() {
        val handler = resolver.findHandler(Country::class, COUNTRY_CREATED_EVENT)
        assertNotNull(handler)
        assertEquals(2, handler?.parameters?.size)
        assertEquals(Country::class, handler?.parameters?.get(0)?.type?.classifier)
        assertEquals(CountryCreatedEvent::class, handler?.parameters?.get(1)?.type?.classifier)
    }

    @Test
    internal fun `resolver should return method with given parameter type`() {
        val handler = resolver.findHandler(Country::class, CountryCreatedEvent::class)
        assertNotNull(handler)
        assertEquals(2, handler?.parameters?.size)
        assertEquals(Country::class, handler?.parameters?.get(0)?.type?.classifier)
        assertEquals(CountryCreatedEvent::class, handler?.parameters?.get(1)?.type?.classifier)
    }

    @Test
    internal fun `resolver should return null if type mismatch`() {
        val handler = resolver.findHandler(Country::class, UUID.randomUUID().toString())
        assertNull(handler)
    }

    @Test
    internal fun `resolver should return null if no annotated handlers`() {
        val handler = resolver.findHandler(EventEntity::class, CountryCreatedEvent::class)
        assertNull(handler)
    }
}
