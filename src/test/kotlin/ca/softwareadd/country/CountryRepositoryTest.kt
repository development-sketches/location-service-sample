package ca.softwareadd.country

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.test.jdbc.JdbcTestUtils
import java.util.*
import javax.sql.DataSource

private const val TABLE_NAME = "country"

@DataJpaTest
internal class CountryRepositoryTest {

    @Autowired
    private lateinit var dataSource: DataSource

    @Autowired
    private lateinit var repository: CountryRepository

    @Test
    internal fun `add country`() {
        val entity = CountryEntity().apply {
            id = UUID.randomUUID()
            alpha2code = "CA"
            alpha3code = "CAD"
            fullName = "Canada"
            numericCode = "123"
            shortName = "Canada"
        }

        repository.saveAndFlush(entity)

        val template = JdbcTemplate(dataSource)

        assertEquals(1, JdbcTestUtils.countRowsInTable(template, TABLE_NAME))
        assertEquals(1, JdbcTestUtils.countRowsInTableWhere(template, TABLE_NAME,
                "id = '${entity.id}'"))
    }

    @Test
    internal fun `alpha2code must be unique`() {
        val entity1 = CountryEntity().apply {
            id = UUID.randomUUID()
            alpha2code = "CA"
            alpha3code = "CAD"
            fullName = "Canada"
            numericCode = "123"
            shortName = "Canada"
        }
        val entity2 = CountryEntity().apply {
            id = UUID.randomUUID()
            alpha2code = "CA"
            alpha3code = "USA"
            fullName = "United States of America"
            numericCode = "14"
            shortName = "United States"
        }
        repository.saveAndFlush(entity1)
        assertThrows(DataIntegrityViolationException::class.java) {
            repository.saveAndFlush(entity2)
        }
    }

    @Test
    internal fun `alpha3code must be unique`() {
        val entity1 = CountryEntity().apply {
            id = UUID.randomUUID()
            alpha2code = "CA"
            alpha3code = "CAD"
            fullName = "Canada"
            numericCode = "123"
            shortName = "Canada"
        }
        val entity2 = CountryEntity().apply {
            id = UUID.randomUUID()
            alpha2code = "US"
            alpha3code = "CAD"
            fullName = "United States of America"
            numericCode = "14"
            shortName = "United States"
        }
        repository.saveAndFlush(entity1)
        assertThrows(DataIntegrityViolationException::class.java) {
            repository.saveAndFlush(entity2)
        }
    }

    @Test
    internal fun `fullName must unique`() {
        val entity1 = CountryEntity().apply {
            id = UUID.randomUUID()
            alpha2code = "CA"
            alpha3code = "CAD"
            fullName = "Canada"
            numericCode = "123"
            shortName = "Canada"
        }
        val entity2 = CountryEntity().apply {
            id = UUID.randomUUID()
            alpha2code = "US"
            alpha3code = "USA"
            fullName = "Canada"
            numericCode = "14"
            shortName = "United States"
        }
        repository.saveAndFlush(entity1)
        assertThrows(DataIntegrityViolationException::class.java) {
            repository.saveAndFlush(entity2)
        }
    }

    @Test
    internal fun `numericCode must be unique`() {
        val entity1 = CountryEntity().apply {
            id = UUID.randomUUID()
            alpha2code = "CA"
            alpha3code = "CAD"
            fullName = "Canada"
            numericCode = "123"
            shortName = "Canada"
        }
        val entity2 = CountryEntity().apply {
            id = UUID.randomUUID()
            alpha2code = "US"
            alpha3code = "USA"
            fullName = "United States of America"
            numericCode = "123"
            shortName = "United States"
        }
        repository.saveAndFlush(entity1)
        assertThrows(DataIntegrityViolationException::class.java) {
            repository.saveAndFlush(entity2)
        }
    }

    @Test
    internal fun `shortName must be unique`() {
        val entity1 = CountryEntity().apply {
            id = UUID.randomUUID()
            alpha2code = "CA"
            alpha3code = "CAD"
            fullName = "Canada"
            numericCode = "123"
            shortName = "Canada"
        }
        val entity2 = CountryEntity().apply {
            id = UUID.randomUUID()
            alpha2code = "US"
            alpha3code = "USA"
            fullName = "United States of America"
            numericCode = "14"
            shortName = "Canada"
        }
        repository.saveAndFlush(entity1)
        assertThrows(DataIntegrityViolationException::class.java) {
            repository.saveAndFlush(entity2)
        }
    }
}
