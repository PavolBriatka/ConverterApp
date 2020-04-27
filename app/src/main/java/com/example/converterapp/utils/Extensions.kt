package com.example.converterapp.utils

import kotlin.math.round

fun Double.round(decimals: Int): Double {
    var multiplier = 1.0
    repeat(decimals) { multiplier *= 10 }
    return round(this * multiplier) / multiplier
}

fun <K, V> Map<K, V>.mapToArray(): ArrayList<V> {
    return arrayListOf<V>().apply {
        addAll(this@mapToArray.values)
    }
}