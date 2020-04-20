@file:Suppress("UNCHECKED_CAST")

package ca.softwareadd.utils

import org.mockito.BDDMockito

/**
 *
 * @author Eugene Ossipov
 */
fun <T> uninitialized(): T = null as T

fun <T> eqK(value: T): T = BDDMockito.eq(value) ?: value

fun <T> anyK(): T = BDDMockito.any() ?: (null as T)
