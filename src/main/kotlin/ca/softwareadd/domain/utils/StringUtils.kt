package ca.softwareadd.domain.utils

import java.util.regex.Pattern

private val pattern = Pattern.compile("(?<!(?:^|_))(\\p{Lu}+)").toRegex()

fun String.toSnake(): String = replace(pattern, "_\$1").toLowerCase()
