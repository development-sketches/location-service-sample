package ca.softwareadd.domain.resolvers

import ca.softwareadd.domain.aggregate.Aggregate
import ca.softwareadd.domain.aggregate.EventConsumer
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit.jupiter.SpringExtension
import java.util.*

@ExtendWith(SpringExtension::class)
@ContextConfiguration(classes = [
    StreamResolver::class
])
internal class StreamResolverTest {

    @Autowired
    private lateinit var resolver: StreamResolver

    @Test
    internal fun `get stream from class name`() {
        assertEquals(
                "test_aggregate",
                resolver.streamForClass(TestAggregate::class)
        )
    }

    class TestAggregate(id: UUID, eventConsumer: EventConsumer) : Aggregate(id, eventConsumer)

}
