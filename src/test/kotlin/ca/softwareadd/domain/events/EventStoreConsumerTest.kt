package ca.softwareadd.domain.events

import ca.softwareadd.country.Country
import ca.softwareadd.utils.eqK
import ca.softwareadd.utils.uninitialized
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.ArgumentCaptor
import org.mockito.BDDMockito.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.kafka.support.KafkaHeaders.RECEIVED_MESSAGE_KEY
import org.springframework.kafka.support.KafkaHeaders.RECEIVED_TIMESTAMP
import org.springframework.messaging.support.MessageBuilder
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit.jupiter.SpringExtension
import java.util.*

@ExtendWith(SpringExtension::class)
@ContextConfiguration(classes = [
    EventStoreConsumer::class
])
internal class EventStoreConsumerTest {

    @Autowired
    private lateinit var consumer: EventStoreConsumer

    @MockBean
    private lateinit var eventStore: EventStore

    @Test
    internal fun `test processing event`() {

        val id = UUID.randomUUID()
        given(eventStore.countAllById(Country::class, id)).willReturn(0)

        val message = MessageBuilder
                .withPayload("{}")
                .setHeader(RECEIVED_MESSAGE_KEY, id.toString())
                .setHeader(RECEIVED_TIMESTAMP, 0L)
                .setHeader("event.type", "country-created")
                .build()

        consumer.handleEvent(Country::class, message)

        verify(eventStore, times(1))
                .countAllById(eqK(Country::class), eqK(id))

        val captor = ArgumentCaptor.forClass(EventEntity::class.java)
        verify(eventStore, times(1))
                .save(eqK(Country::class), captor.capture() ?: uninitialized())

        val entity = captor.value
        assertEquals(id, entity.id)
        assertEquals("country-created", entity.type)
        assertEquals(1, entity.version)
    }

}
