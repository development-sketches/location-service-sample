package ca.softwareadd.country

import ca.softwareadd.domain.commands.CommandSender
import ca.softwareadd.utils.anyK
import ca.softwareadd.utils.eqK
import ca.softwareadd.utils.uninitialized
import org.hamcrest.collection.IsCollectionWithSize.hasSize
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.mockito.ArgumentCaptor
import org.mockito.BDDMockito.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.util.*

@WebMvcTest(CountryController::class)
internal class CountryControllerTest {

    @Autowired
    private lateinit var client: MockMvc

    @MockBean
    private lateinit var commandSender: CommandSender

    @MockBean
    private lateinit var queryService: CountryQueryService

    @Test
    internal fun `create a new country`() {
        val request = """
            {
            	"type": "create-country",
            	"alpha2code": "CA",
            	"alpha3code": "CAN",
            	"fullName": "Canada",
            	"numericCode": "123",
            	"shortName": "Canada"
            }
        """.trimIndent()
        client.perform(
                post("/v1/countries")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isAccepted)

        val captor = ArgumentCaptor.forClass(CreateCountryCommand::class.java)

        verify(commandSender, times(1))
                .send(eqK(Country::class), anyK(), captor.capture() ?: uninitialized())

        val command = captor.value

        assertEquals(CREATE_COUNTRY_COMMAND, command.type)
        assertEquals("CA", command.alpha2code)
        assertEquals("CAN", command.alpha3code)
        assertEquals("Canada", command.fullName)
        assertEquals("123", command.numericCode)
        assertEquals("Canada", command.shortName)
    }

    @Test
    internal fun `find all countries`() {
        given(queryService.findAll()).willReturn(listOf(
                object : CountryProjection {
                    override val id: UUID
                        get() = UUID.randomUUID()
                    override val alpha2code: String
                        get() = "CA"
                    override val alpha3code: String
                        get() = "CAN"
                    override val fullName: String
                        get() = "Canada"
                    override val numericCode: String
                        get() = "123"
                    override val shortName: String
                        get() = "Canada"
                }
        ))

        client.perform(get("/v1/countries").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk)
                .andExpect(jsonPath("$").isArray)
                .andExpect(jsonPath("$", hasSize<CountryProjection>(1)))

        verify(queryService, times(1)).findAll()
    }

    @Test
    internal fun `find a range of countries`() {
        val content = listOf(
                object : CountryProjection {
                    override val id: UUID
                        get() = UUID.randomUUID()
                    override val alpha2code: String
                        get() = "CA"
                    override val alpha3code: String
                        get() = "CAN"
                    override val fullName: String
                        get() = "Canada"
                    override val numericCode: String
                        get() = "123"
                    override val shortName: String
                        get() = "Canada"
                }
        )
        given(queryService.findAll(anyK())).willAnswer {
            PageImpl(
                    content,
                    it.arguments[0] as Pageable,
                    1L
            )
        }

        client.perform(get("/v1/countries").param("page", "0")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk)
                .andExpect(jsonPath("$.content").isArray)
                .andExpect(jsonPath("$.content", hasSize<CountryProjection>(1)))

        verify(queryService, times(1)).findAll(anyK())

    }
}
