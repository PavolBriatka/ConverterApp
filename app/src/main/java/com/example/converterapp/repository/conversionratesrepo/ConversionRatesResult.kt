package com.example.converterapp.repository.conversionratesrepo


data class ConversionRatesResult(val conversionRates: Map<String, Currency>) {

    data class Currency(val currencyCode: String, val currencyName: String, val relativeRate: Double, val flagId: Int)
}