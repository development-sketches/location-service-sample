package ca.softwareadd.domain.resolvers

import ca.softwareadd.country.CountryProjectionHandler
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit.jupiter.SpringExtension
import kotlin.reflect.jvm.javaMethod

private const val COUNTRY_CREATED_EVENT = "country-created"

@ExtendWith(SpringExtension::class)
@ContextConfiguration(classes = [
    ProjectionHandlerResolver::class,
    EventTypeResolver::class
])
internal class ProjectionHandlerResolverTest {

    @Autowired
    private lateinit var resolver: ProjectionHandlerResolver

    @Test
    internal fun `test finding event handler`() {
        val handler = resolver.findHandler(CountryProjectionHandler::class, COUNTRY_CREATED_EVENT)
        assertNotNull(handler)
        assertEquals(CountryProjectionHandler::countryCreated.javaMethod?.name, handler?.name)
    }
}
