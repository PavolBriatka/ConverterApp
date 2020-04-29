package com.example.converterapp.repository.conversionratesrepo

import com.example.converterapp.repository.conversionratesrepo.ConversionRatesResult.*

object CurrencyMapCache {

    private var cachedData = mapOf<String, Currency>()

    fun saveData(data: Map<String, Currency>) {
        cachedData = data
    }

    fun retrieveData(): Map<String,Currency> {
        return  cachedData
    }
}