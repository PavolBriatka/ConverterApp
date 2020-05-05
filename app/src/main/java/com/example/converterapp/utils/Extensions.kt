package com.example.converterapp.utils

import com.revolut.rxdata.dod.Data
import io.reactivex.Observable
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

fun <T> Observable<Data<T>>.extractData(): Observable<T> =
    filter { it.content != null }.map { it.content!! }