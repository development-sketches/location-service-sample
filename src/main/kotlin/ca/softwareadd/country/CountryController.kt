package ca.softwareadd.country

import ca.softwareadd.domain.commands.CommandSender
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RestController
@RequestMapping("/v1/countries")
class CountryController(
        private val commandSender: CommandSender
) {

    @PostMapping
    fun crate(@RequestBody command: CreateCountryCommand): ResponseEntity<Revision> {
        val revision = Revision()
        commandSender.send(Country::class, revision.id, command)
        return ResponseEntity.accepted().body(revision)
    }

    data class Revision(val id: UUID = UUID.randomUUID(), val version: Int = 0)
}
