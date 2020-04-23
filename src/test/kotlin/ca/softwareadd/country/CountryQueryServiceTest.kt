package ca.softwareadd.country

import ca.softwareadd.utils.anyK
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit.jupiter.SpringExtension
import java.util.*

@ExtendWith(SpringExtension::class)
@ContextConfiguration(classes = [
    CountryQueryService::class
])
internal class CountryQueryServiceTest {

    @Autowired
    private lateinit var queryService: CountryQueryService

    @MockBean
    private lateinit var repository: CountryRepository

    @Test
    internal fun `service returns all the records`() {

        given(repository.findAll()).willReturn(emptyList())

        queryService.findAll()

        verify(repository, times(1)).findAll()
    }

    @Test
    internal fun `service returns a range of records`() {
        val entity = CountryEntity().apply {
            id = UUID.randomUUID()
            alpha2code = "CA"
            alpha3code = "CAN"
            fullName = "Canada"
            numericCode = "123"
            shortName = "Canada"
        }
        val pageRequest = PageRequest.of(0, 20)
        val source = PageImpl(listOf(entity), pageRequest, 1L)

        given(repository.findAll(anyK<Pageable>())).willReturn(source)

        val page = queryService.findAll(pageRequest)

        assertEquals(1, page.totalElements)
        assertEquals(1, page.content.size)

        val projection = page.content[0]
        assertTrue(entity === projection)
    }
}
