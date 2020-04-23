package com.example.converterapp.webservice.conversionratesinteractor

data class ConversionRatesResponseModel(val baseCurrency: String, val rates: Map<String, Double>)