package ca.softwareadd.country

class CountryCreatedEvent(
        val alpha2code: String,
        val alpha3code: String,
        val fullName: String,
        val numericCode: String,
        val shortName: String
)
