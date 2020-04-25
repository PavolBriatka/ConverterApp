package com.example.converterapp.webservice.conversionratesinteractor

data class ConversionRatesResponseModel(val baseCurrency: String? = null, val rates: Map<String, Double>? = null)