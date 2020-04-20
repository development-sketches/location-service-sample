package ca.softwareadd.country

import java.util.*

interface CountryProjection {

    val id: UUID

    val alpha2code: String

    val alpha3code: String

    val fullName: String

    val numericCode: String

    val shortName: String
}
