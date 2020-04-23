package ca.softwareadd.domain.events

import ca.softwareadd.domain.aggregate.Aggregate
import ca.softwareadd.domain.resolvers.StreamResolver
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Repository
import java.time.OffsetDateTime
import java.util.*
import javax.sql.DataSource
import kotlin.reflect.KClass


@Repository
class JdbcEventStore(
        dataSource: DataSource,
        private val streamResolver: StreamResolver
) : EventStore {

    private val jdbcTemplate = NamedParameterJdbcTemplate(dataSource)

    override fun <T : Aggregate> countAllById(klass: KClass<T>, id: UUID): Int {
        val stream = streamResolver.streamForClass(klass)
        val sql = "select count(*) from ${stream}_event where id = :id"
        return jdbcTemplate.queryForObject(sql, mapOf("id" to id), Int::class.java) ?: 0
    }

    override fun <T : Aggregate> findAllById(klass: KClass<T>, id: UUID): List<EventEntity> {
        val stream = streamResolver.streamForClass(klass)
        val sql = "select * from ${stream}_event where id = :id order by version"
        return jdbcTemplate.query(sql, mapOf("id" to id)) { rs, _ ->
            EventEntity().apply {
                this.id = rs.getObject("id", UUID::class.java)
                version = rs.getInt("version")
                type = rs.getString("type")
                timestamp = rs.getObject("timestamp", OffsetDateTime::class.java).toZonedDateTime()
                userId = rs.getObject("user_id", UUID::class.java)
                json = rs.getString("json")
            }
        }
    }

    override fun <T : Aggregate> save(klass: KClass<T>, entity: EventEntity) {
        val stream = streamResolver.streamForClass(klass)
        val sql = """
                    insert into ${stream}_event(id, version, type, timestamp, user_id, json) 
                        values (:id, :version, :type, :timestamp, :userId, :json)
                """.trimIndent()
        val params = MapSqlParameterSource().apply {
            addValue(entity::id.name, entity.id)
            addValue(entity::version.name, entity.version)
            addValue(entity::type.name, entity.type)
            addValue(entity::timestamp.name, entity.timestamp.toOffsetDateTime())
            addValue(entity::userId.name, entity.userId)
            addValue(entity::json.name, entity.json)
        }
        jdbcTemplate.update(sql, params)
    }

}
