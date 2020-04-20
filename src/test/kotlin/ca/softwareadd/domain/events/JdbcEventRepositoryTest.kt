package ca.softwareadd.domain.events

import ca.softwareadd.country.COUNTRY_CREATED_EVENT
import ca.softwareadd.country.Country
import ca.softwareadd.country.CountryCreatedEvent
import ca.softwareadd.domain.resolvers.StreamResolver
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.jdbc.Sql
import org.springframework.test.jdbc.JdbcTestUtils.countRowsInTable
import org.springframework.test.jdbc.JdbcTestUtils.countRowsInTableWhere
import java.time.ZonedDateTime
import java.util.*
import javax.sql.DataSource

@JdbcTest
@ContextConfiguration(classes = [
    StreamResolver::class,
    JdbcEventRepository::class
])
internal class JdbcEventRepositoryTest {

    @Autowired
    private lateinit var dataSource: DataSource

    @Autowired
    private lateinit var repository: JdbcEventRepository

    @Test
    internal fun `test adding a new event`() {
        val stream = "country"
        val tableName = "${stream}_event"
        val event = CountryCreatedEvent("CA", "CAN", "Canada", "123", "Canada")
        val entity = EventEntity().apply {
            id = UUID.randomUUID()
            type = event.type
            timestamp = ZonedDateTime.now()
            json = ObjectMapper().writeValueAsString(event)
        }
        repository.save(Country::class, entity)

        val template = JdbcTemplate(dataSource)

        assertEquals(1, countRowsInTable(template, tableName))
        assertEquals(1, countRowsInTableWhere(template, tableName,
                "id = '${entity.id}' and version = ${entity.version}"))
    }

    @Test
    internal fun `test duplicate events`() {
        val event = CountryCreatedEvent("CA", "CAN", "Canada", "123", "Canada")
        val entity = EventEntity().apply {
            id = UUID.randomUUID()
            type = event.type
            timestamp = ZonedDateTime.now()
            json = ObjectMapper().writeValueAsString(event)
        }
        repository.save(Country::class, entity)
        assertThrows(org.springframework.dao.DuplicateKeyException::class.java) {
            repository.save(Country::class, entity)
        }
    }

    @Test
    @Sql("/create-countries.sql")
    internal fun `test counting events by id`() {
        val id = UUID.fromString("68371135-46d4-436f-92b0-aa5919531f16")
        val numberOfEvents = repository.countAllById(Country::class, id)
        assertEquals(2, numberOfEvents)
    }

    @Test
    @Sql("/create-countries.sql")
    internal fun `test finding events by id`() {
        val id = UUID.fromString("68371135-46d4-436f-92b0-aa5919531f16")
        val history = repository.findAllById(Country::class, id)
        assertEquals(2, history.size)

        assertEquals(id, history[0].id)
        assertEquals(0, history[0].version)
        assertEquals(COUNTRY_CREATED_EVENT, history[0].type)

        assertEquals(id, history[1].id)
        assertEquals(1, history[1].version)
        assertEquals(COUNTRY_CREATED_EVENT, history[1].type)
    }
}
