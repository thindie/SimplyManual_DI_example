package com.thindie.simplyweather.presentation

import java.time.format.DateTimeFormatter
import java.time.temporal.TemporalAccessor

fun getTemporalAccessor(string: String): TemporalAccessor = DateTimeFormatter
    .ofPattern("yyyy-MM-dd'T'HH:mm")
    .parse(string)

fun TemporalAccessor.toUiString(pattern: String): String =
    try {
        DateTimeFormatter.ofPattern(pattern)
            .format(this)
    } catch (_: Exception) {
        "???"
    }
