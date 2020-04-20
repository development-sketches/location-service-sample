package ca.softwareadd.country

import ca.softwareadd.domain.events.Event

const val COUNTRY_CREATED_EVENT = "country-created"

abstract class CountryEvent(override val type: String) : Event

class CountryCreatedEvent(
        val alpha2code: String,
        val alpha3code: String,
        val fullName: String,
        val numericCode: String,
        val shortName: String
) : CountryEvent(COUNTRY_CREATED_EVENT)
