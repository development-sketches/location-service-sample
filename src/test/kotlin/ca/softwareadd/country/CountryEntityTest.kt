package ca.softwareadd.country

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotEquals
import org.junit.jupiter.api.Test
import java.util.*

internal class CountryEntityTest {

    @Test
    internal fun `test same entities`() {
        val entity1 = CountryEntity().apply {
            id = UUID.randomUUID()
            alpha2code = "CA"
            alpha3code = "CAD"
            fullName = "Canada"
            numericCode = "123"
            shortName = "Canada"
        }
        val entity2 = CountryEntity().apply {
            id = entity1.id
            alpha2code = "RU"
            alpha3code = "RUS"
            fullName = "Russia"
            numericCode = "123"
            shortName = "Russia"
        }
        assertEquals(entity1, entity2)
        assertEquals(entity1.hashCode(), entity2.hashCode())
    }

    @Test
    internal fun `test different entities`() {
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
            alpha3code = "CAD"
            fullName = "Canada"
            numericCode = "123"
            shortName = "Canada"
        }
        assertNotEquals(entity1, entity2)
    }
}
