package ca.softwareadd.country

import java.util.*
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "country")
class CountryEntity : CountryProjection {

    @Id
    override lateinit var id: UUID

    override lateinit var alpha2code: String

    override lateinit var alpha3code: String

    override lateinit var fullName: String

    override lateinit var numericCode: String

    override lateinit var shortName: String

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is CountryEntity) return false

        return id == other.id
    }

    override fun hashCode(): Int = id.hashCode()

}
