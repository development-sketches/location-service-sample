package ca.softwareadd.country

import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface CountryRepository : JpaRepository<CountryEntity, UUID>
