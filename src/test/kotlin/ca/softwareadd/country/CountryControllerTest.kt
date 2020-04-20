package ca.softwareadd.country

import ca.softwareadd.domain.commands.CommandSender
import ca.softwareadd.utils.anyK
import ca.softwareadd.utils.eqK
import ca.softwareadd.utils.uninitialized
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.mockito.ArgumentCaptor
import org.mockito.BDDMockito.times
import org.mockito.BDDMockito.verify
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WebMvcTest(CountryController::class)
internal class CountryControllerTest {

    @Autowired
    private lateinit var client: MockMvc

    @MockBean
    private lateinit var commandSender: CommandSender

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
}
