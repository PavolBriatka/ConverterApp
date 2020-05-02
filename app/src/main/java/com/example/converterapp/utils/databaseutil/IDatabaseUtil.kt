package com.example.converterapp.utils.databaseutil

import com.example.converterapp.repository.conversionratesrepo.ConversionRatesResult

interface IDatabaseUtil {

    fun convertAndSave(data: ConversionRatesResult)
    fun retrieveAndConvert(): ConversionRatesResult
}