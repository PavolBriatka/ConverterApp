package com.example.converterapp.utils.currencyhelper

interface ICurrencyHelper {

    fun fetchResources(code: String): Pair<String, Int>?
}