package com.example.converterapp.utils

import com.example.converterapp.repository.conversionratesrepo.ConversionRatesResult

interface IDatabaseUtil {

    fun convertAndSave(data: ConversionRatesResult)
    fun retrieveAndConvert(): ConversionRatesResult
}