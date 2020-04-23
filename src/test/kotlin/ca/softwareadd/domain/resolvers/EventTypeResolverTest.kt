package ca.softwareadd.domain.resolvers

import ca.softwareadd.domain.events.Event
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit.jupiter.SpringExtension

@ExtendWith(SpringExtension::class)
@ContextConfiguration(classes = [
    EventTypeResolver::class
])
internal class EventTypeResolverTest {

    @Autowired
    private lateinit var resolver: EventTypeResolver

    @Test
    internal fun `event type is derived from the class name with event suffix`() {
        assertEquals(
                "simple",
                resolver.eventType(SimpleEvent::class)
        )
    }

    @Test
    internal fun `event type derived from the class name without event suffix`() {
        assertEquals(
                "simple-pojo",
                resolver.eventType(SimplePojo::class)
        )
    }

    @Test
    internal fun `event type is taken from the annotation`() {
        assertEquals(
                "annotated-event",
                resolver.eventType(AnnotatedEvent::class)
        )
    }

    @Test
    internal fun `event type derived from the annotated class`() {
        assertEquals(
                "annotated-no-type",
                resolver.eventType(AnnotatedNoTypeEvent::class)
        )
    }

    class SimpleEvent

    class SimplePojo

    @Event("annotated-event")
    class AnnotatedEvent

    @Event
    class AnnotatedNoTypeEvent
}
