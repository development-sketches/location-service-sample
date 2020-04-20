package ca.softwareadd.country

import ca.softwareadd.domain.commands.Command

const val CREATE_COUNTRY_COMMAND = "create-country"

abstract class CountryCommand(override val type: String) :
        Command

class CreateCountryCommand : CountryCommand(CREATE_COUNTRY_COMMAND) {

    lateinit var alpha2code: String

    lateinit var alpha3code: String

    lateinit var fullName: String

    lateinit var numericCode: String

    lateinit var shortName: String

}
