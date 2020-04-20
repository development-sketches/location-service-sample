package ca.softwareadd.domain.events

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotEquals
import org.junit.jupiter.api.Test
import java.util.*

internal class EventEntityTest {

    @Test
    internal fun `entities are equal if they have equal IDs and versions`() {
        val entity1 = EventEntity().apply {
            id = UUID.randomUUID()
            version = 0
        }
        val entity2 = EventEntity().apply {
            id = entity1.id
            version = 0
        }
        assertEquals(entity1, entity2)
        assertEquals(entity1.hashCode(), entity2.hashCode())
    }

    @Test
    internal fun `entities are not equal if their IDs are different`() {
        val entity1 = EventEntity().apply {
            id = UUID.randomUUID()
            version = 0
        }
        val entity2 = EventEntity().apply {
            id = UUID.randomUUID()
            version = 0
        }
        assertNotEquals(entity1, entity2)
    }

    @Test
    internal fun `entities are not equal if their versions are different`() {
        val entity1 = EventEntity().apply {
            id = UUID.randomUUID()
            version = 0
        }
        val entity2 = EventEntity().apply {
            id = entity1.id
            version = 1
        }
        assertNotEquals(entity1, entity2)
    }
}
