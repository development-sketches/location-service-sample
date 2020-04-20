package ca.softwareadd.domain.resolvers

import ca.softwareadd.domain.aggregate.Aggregate
import ca.softwareadd.domain.events.EventEntity
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit.jupiter.SpringExtension
import java.util.*

@ExtendWith(SpringExtension::class)
@ContextConfiguration(classes = [AggregateConstructorResolver::class])
internal class AggregateConstructorResolverTest {

    @Autowired
    private lateinit var resolver: AggregateConstructorResolver

    @Test
    internal fun `resolver should return a constructor with two parameters`() {
        val constructor = resolver.findConstructor(Aggregate::class, UUID::class)
        assertNotNull(constructor)
        assertEquals(2, constructor.parameters.size)
        assertEquals(UUID::class, constructor.parameters[0].type.classifier)
    }

    @Test
    internal fun `resolver should throw NoSuchElementException if constructor is not found`() {
        assertThrows(NoSuchElementException::class.java) {
            resolver.findConstructor(EventEntity::class, UUID::class)
        }
    }
}
