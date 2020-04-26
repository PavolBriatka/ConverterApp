package com.example.converterapp.repository.conversionratesrepo


data class ConversionRatesResult(val conversionRates: ArrayList<Currency>) {

    data class Currency(val currencyCode: String, val relativeRate: Double)
}