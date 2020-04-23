package ca.softwareadd.country

import ca.softwareadd.domain.events.EventHandler
import ca.softwareadd.domain.projections.ProjectionHandler
import ca.softwareadd.domain.routers.ProjectionRouter
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.util.*

@Service
class CountryProjectionHandler(
        router: ProjectionRouter,
        private val repository: CountryRepository
) : ProjectionHandler(router) {

    private val logger: Logger = LoggerFactory.getLogger(this::class.java)

    @EventHandler
    fun countryCreated(id: UUID, event: CountryCreatedEvent) {
        try {
            val entity = CountryEntity().apply {
                this.id = id
                alpha2code = event.alpha2code
                alpha3code = event.alpha3code
                fullName = event.fullName
                numericCode = event.numericCode
                shortName = event.shortName
            }
            repository.save(entity)
        } catch (ex: Exception) {
            logger.error(ex.localizedMessage, ex)
        }
    }

}
