package ca.softwareadd.domain.routers

import ca.softwareadd.country.CountryCreatedEvent
import ca.softwareadd.country.CountryProjectionHandler
import ca.softwareadd.domain.resolvers.EventTypeResolver
import ca.softwareadd.domain.resolvers.ProjectionHandlerResolver
import ca.softwareadd.utils.eqK
import ca.softwareadd.utils.uninitialized
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.ArgumentCaptor
import org.mockito.BDDMockito.times
import org.mockito.BDDMockito.verify
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

private const val COUNTRY_CREATED_EVENT = "country-created"

@ExtendWith(SpringExtension::class)
@ContextConfiguration(classes = [
    ProjectionRouter::class,
    ProjectionHandlerResolver::class,
    EventTypeResolver::class
])
@Import(ProjectionRouterTest.TestConfiguration::class)
internal class ProjectionRouterTest {

    @Autowired
    private lateinit var router: ProjectionRouter

    @MockBean
    private lateinit var handler: CountryProjectionHandler

    @Autowired
    private lateinit var mapper: ObjectMapper

    @Test
    internal fun `test routing messages`() {
        val id = UUID.randomUUID()
        val event = CountryCreatedEvent(
                "CA",
                "CAN",
                "Canada",
                "123",
                "Canada"
        )
        val message = MessageBuilder
                .withPayload(mapper.writeValueAsString(event))
                .setHeader(RECEIVED_MESSAGE_KEY, id.toString())
                .setHeader("event.type", COUNTRY_CREATED_EVENT)
                .build()
        router.handle(handler, message)

        val captor = ArgumentCaptor.forClass(CountryCreatedEvent::class.java)
        verify(handler, times(1))
                .countryCreated(eqK(id), captor.capture() ?: uninitialized())

        val received = captor.value
        assertEquals(event.alpha2code, received.alpha2code)
        assertEquals(event.alpha3code, received.alpha3code)
        assertEquals(event.fullName, received.fullName)
        assertEquals(event.numericCode, received.numericCode)
        assertEquals(event.shortName, received.shortName)
    }

    @Configuration
    class TestConfiguration {

        @Bean
        fun mapper(): ObjectMapper = ObjectMapper().registerModule(KotlinModule())
    }
}
