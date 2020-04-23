package ca.softwareadd.country

import ca.softwareadd.domain.aggregate.Aggregate
import ca.softwareadd.domain.aggregate.EventConsumer
import ca.softwareadd.domain.commands.CommandHandler
import ca.softwareadd.domain.events.EventHandler
import java.util.*

enum class CountryAggregateState {
    NEW,
    EXISTING,
    DELETED
}

class Country(
        id: UUID,
        eventConsumer: EventConsumer
) : Aggregate(id, eventConsumer) {

    lateinit var alpha2code: String
        private set

    lateinit var alpha3code: String
        private set

    lateinit var fullName: String
        private set

    lateinit var numericCode: String
        private set

    lateinit var shortName: String
        private set

    private var state = CountryAggregateState.NEW

    @EventHandler
    fun countryCreated(event: CountryCreatedEvent) {
        alpha2code = event.alpha2code
        alpha3code = event.alpha3code
        fullName = event.fullName
        numericCode = event.numericCode
        shortName = event.shortName

        state = CountryAggregateState.EXISTING
    }

    @CommandHandler
    fun createCountry(command: CreateCountryCommand) {
        if (state == CountryAggregateState.NEW) {
            with(command) {
                applyEvent(CountryCreatedEvent(alpha2code, alpha3code, fullName, numericCode, shortName))
            }
        }
    }

}
