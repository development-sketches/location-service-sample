package ca.softwareadd.country

import ca.softwareadd.domain.routers.ProjectionRouter
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.ArgumentCaptor
import org.mockito.BDDMockito.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit.jupiter.SpringExtension
import java.util.*

@ExtendWith(SpringExtension::class)
@ContextConfiguration(classes = [
    CountryProjectionHandler::class
])
internal class CountryProjectionHandlerTest {

    @Autowired
    private lateinit var handler: CountryProjectionHandler

    @MockBean
    private lateinit var repository: CountryRepository

    @MockBean
    private lateinit var router: ProjectionRouter

    @Test
    internal fun `test creating country`() {
        val id = UUID.randomUUID()
        val event = CountryCreatedEvent(
                "CA",
                "CAN",
                "Canada",
                "123",
                "Canada"
        )
        handler.countryCreated(id, event)

        val captor = ArgumentCaptor.forClass(CountryEntity::class.java)

        verify(repository, times(1)).save(captor.capture())

        val entity = captor.value
        assertEquals(id, entity.id)
        assertEquals(event.alpha2code, entity.alpha2code)
        assertEquals(event.alpha3code, entity.alpha3code)
        assertEquals(event.fullName, entity.fullName)
        assertEquals(event.numericCode, entity.numericCode)
        assertEquals(event.shortName, entity.shortName)

        verifyNoInteractions(router)
    }
}
