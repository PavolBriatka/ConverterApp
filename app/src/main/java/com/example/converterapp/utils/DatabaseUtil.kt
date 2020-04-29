package com.example.converterapp.utils

import com.example.converterapp.database.AppDatabase
import com.example.converterapp.database.CurrencyEntity
import com.example.converterapp.repository.conversionratesrepo.ConversionRatesResult
import com.example.converterapp.repository.conversionratesrepo.ConversionRatesResult.Currency
import javax.inject.Inject

class DatabaseUtil @Inject constructor(private val appDatabase: AppDatabase?) {

    fun convertAndSave(data: ConversionRatesResult) {

        appDatabase?.let { database ->
            val dao = database.currencyDao()
            val convertedData = data.conversionRates.mapToArray()

            convertedData.forEach { currency ->
                dao.saveCurrency(
                    CurrencyEntity(
                        currencyCode = currency.currencyCode,
                        currencyName = currency.currencyName,
                        currencyRate = currency.relativeRate,
                        flagId = currency.flagId
                    )
                )
            }
        }
    }

    fun retrieveAndConvert(): ConversionRatesResult {

        appDatabase?.let { database ->
            val dao = database.currencyDao()
            val convertedData = convertEntity(dao.retrieveCurrencies()).associateBy { it.currencyCode }

            return ConversionRatesResult(convertedData)
        }

        return ConversionRatesResult(mapOf())
    }

    private fun convertEntity(data: List<CurrencyEntity>): ArrayList<Currency> {
        return arrayListOf<Currency>().apply {
            data.forEach {
                add(
                    Currency(
                        currencyCode = it.currencyCode,
                        currencyName = it.currencyName,
                        relativeRate = it.currencyRate,
                        flagId = it.flagId
                    )
                )
            }
        }
    }
}