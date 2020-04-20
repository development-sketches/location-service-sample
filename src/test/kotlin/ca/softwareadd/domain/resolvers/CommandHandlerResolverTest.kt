package ca.softwareadd.domain.resolvers

import ca.softwareadd.country.CREATE_COUNTRY_COMMAND
import ca.softwareadd.country.Country
import ca.softwareadd.country.CreateCountryCommand
import ca.softwareadd.domain.events.EventEntity
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit.jupiter.SpringExtension
import java.util.*

@ExtendWith(SpringExtension::class)
@ContextConfiguration(classes = [CommandHandlerResolver::class])
internal class CommandHandlerResolverTest {

    @Autowired
    private lateinit var resolver: CommandHandlerResolver

    @Test
    internal fun `resolver should return method annotated with CommandHandler`() {
        val handler = resolver.findHandler(Country::class, CREATE_COUNTRY_COMMAND)
        assertNotNull(handler)
        assertEquals(2, handler?.parameters?.size)
        assertEquals(Country::class, handler?.parameters?.get(0)?.type?.classifier)
        assertEquals(CreateCountryCommand::class, handler?.parameters?.get(1)?.type?.classifier)
    }

    @Test
    internal fun `resolver should return null if type is not found`() {
        val handler = resolver.findHandler(Country::class, UUID.randomUUID().toString())
        assertNull(handler)
    }

    @Test
    internal fun `resolver should return null if no annotated handlers`() {
        val handler = resolver.findHandler(EventEntity::class, CREATE_COUNTRY_COMMAND)
        assertNull(handler)
    }
}
