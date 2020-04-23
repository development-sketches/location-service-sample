package ca.softwareadd.domain.commands

import kotlin.annotation.AnnotationRetention.RUNTIME
import kotlin.annotation.AnnotationTarget.CONSTRUCTOR
import kotlin.annotation.AnnotationTarget.FUNCTION

@Target(CONSTRUCTOR, FUNCTION)
@Retention(RUNTIME)
annotation class CommandHandler
