package ca.softwareadd.domain.events

import kotlin.annotation.AnnotationRetention.RUNTIME
import kotlin.annotation.AnnotationTarget.CLASS

@Target(CLASS)
@Retention(RUNTIME)
annotation class Event(val type: String = "")
