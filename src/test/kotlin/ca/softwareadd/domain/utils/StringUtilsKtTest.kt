package ca.softwareadd.domain.utils

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class StringUtilsKtTest {

    @Test
    fun `make snake from camel case`() {
        assertEquals(
                "get_some_sample",
                "GetSomeSample".toSnake()
        )
    }

    @Test
    fun `make snake from camel case with multiple capitals`() {
        assertEquals(
                "get_posome_sample",
                "GetPOSomeSample".toSnake()
        )
    }

    @Test
    fun `make snake from snake`() {
        assertEquals(
                "get_some_sample",
                "Get_Some_Sample".toSnake()
        )
    }

}
