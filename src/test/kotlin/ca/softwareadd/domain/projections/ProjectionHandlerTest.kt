package ca.softwareadd.domain.projections

import ca.softwareadd.domain.routers.ProjectionRouter
import ca.softwareadd.utils.eqK
import ca.softwareadd.utils.uninitialized
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.mockito.ArgumentCaptor
import org.mockito.BDDMockito.*
import org.springframework.messaging.Message
import org.springframework.messaging.support.MessageBuilder

private const val COUNTRY_CREATED_EVENT = "country-created"

internal class ProjectionHandlerTest {

    private val router: ProjectionRouter = mock(ProjectionRouter::class.java)

    private val handler = ProjectionHandler(router)

    @Suppress("UNCHECKED_CAST")
    @Test
    internal fun `test handling events`() {
        val message = MessageBuilder
                .withPayload("{}")
                .setHeader("event.type", COUNTRY_CREATED_EVENT)
                .build()
        handler.handle(message)

        val captor: ArgumentCaptor<Message<String>> = ArgumentCaptor.forClass(Message::class.java)
                as ArgumentCaptor<Message<String>>
        verify(router, times(1))
                .handle(eqK(handler), captor.capture() ?: uninitialized())

        assertTrue(message === captor.value)
    }
}
