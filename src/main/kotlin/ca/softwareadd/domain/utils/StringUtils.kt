package ca.softwareadd.domain.utils

import java.util.regex.Pattern

private val pattern = Pattern.compile("(?<!(?:^|_))(\\p{Lu}+)").toRegex()

fun String.toSnake(char: Char = '_'): String = replace(pattern, "$char\$1").toLowerCase()
