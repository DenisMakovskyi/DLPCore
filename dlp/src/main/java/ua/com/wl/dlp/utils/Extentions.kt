package ua.com.wl.dlp.utils

import okhttp3.Request

import ua.com.wl.dlp.data.api.responses.PagedResponse

/**
 * @author Denis Makovskyi
 */

fun PagedResponse<*>.previousPage(): Int? =
    previousPage?.let { getQueryParam(it, "page") }?.toInt()

fun PagedResponse<*>.nextPage(): Int? =
    nextPage?.let { getQueryParam(it, "page") }?.toInt()

internal infix fun Int.isEqualsTo(another: Int): Boolean = this == another

internal infix fun Int.isGreaterThan(another: Int): Boolean = this > another

internal infix fun Long.isGreaterThan(another: Int): Boolean = this > another

internal inline fun <T> T.only(block: (T) -> Unit) = block(this)

internal fun CharSequence?.isNonNullOrEmpty(): Boolean = this != null && this.isNotEmpty()

internal fun String?.toJwt(): String = "JWT $this"

internal fun Request.hasHeader(name: String, value: String): Boolean =
    header(name)?.let { it -> it.isNotEmpty() && it == value } ?: false