package ca.softwareadd.domain.events

import ca.softwareadd.domain.aggregate.Aggregate
import java.util.*
import kotlin.reflect.KClass

interface EventRepository {

    fun <T : Aggregate> countAllById(klass: KClass<T>, id: UUID): Int

    fun <T : Aggregate> findAllById(klass: KClass<T>, id: UUID): List<EventEntity>

    fun <T : Aggregate> save(klass: KClass<T>, entity: EventEntity)

}
