package ca.softwareadd.domain.projections

import ca.softwareadd.domain.routers.ProjectionRouter
import org.springframework.messaging.Message

open class ProjectionHandler(
        private val router: ProjectionRouter
) {

    fun handle(message: Message<String>) {
        router.handle(this, message)
    }

}
