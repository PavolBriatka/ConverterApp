package com.example.converterapp.repository.interactor

data class ConversionRatesResponseModel(val baseCurrency: String, val rates: Map<String, Double>)