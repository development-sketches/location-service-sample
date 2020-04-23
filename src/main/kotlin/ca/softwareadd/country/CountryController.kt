package ca.softwareadd.country

import ca.softwareadd.domain.commands.CommandSender
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/v1/countries")
class CountryController(
        private val commandSender: CommandSender,
        private val queryService: CountryQueryService
) {

    @PostMapping
    fun crate(@RequestBody command: CreateCountryCommand): ResponseEntity<Revision> {
        val revision = Revision()
        commandSender.send(Country::class, revision.id, command)
        return ResponseEntity.accepted().body(revision)
    }

    @GetMapping
    fun findAll(): List<CountryProjection> = queryService.findAll()

    @GetMapping(params = ["page"])
    fun findAll(@PageableDefault(size = 25) pageable: Pageable): Page<CountryProjection> =
            queryService.findAll(pageable)

    data class Revision(val id: UUID = UUID.randomUUID(), val version: Int = 0)
}
