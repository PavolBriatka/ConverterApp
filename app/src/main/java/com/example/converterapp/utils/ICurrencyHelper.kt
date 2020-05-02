package com.example.converterapp.utils

interface ICurrencyHelper {

    fun fetchResources(code: String): Pair<String, Int>?
}