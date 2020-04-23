package ca.softwareadd.country

import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

@Service
class CountryQueryService(
        private val repository: CountryRepository
) {

    fun findAll(): List<CountryProjection> = repository.findAll()

    fun findAll(pageable: Pageable): Page<CountryProjection> =
            repository.findAll(pageable).run {
                PageImpl(
                        content as List<CountryProjection>,
                        pageable,
                        totalElements
                )
            }

}
