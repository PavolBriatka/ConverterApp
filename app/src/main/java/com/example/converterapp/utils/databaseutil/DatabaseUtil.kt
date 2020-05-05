package com.example.converterapp.utils.databaseutil

import android.content.SharedPreferences
import com.example.converterapp.database.AppDatabase
import com.example.converterapp.database.CurrencyEntity
import com.example.converterapp.repository.conversionratesrepo.ConversionRatesResult
import com.example.converterapp.repository.conversionratesrepo.ConversionRatesResult.Currency
import com.example.converterapp.utils.mapToArray
import javax.inject.Inject

class DatabaseUtil @Inject constructor(private val appDatabase: AppDatabase?,
 private val sharedPreferences: SharedPreferences) :
    IDatabaseUtil {

    companion object {
        private const val IS_FIRST_TIME_OPEN = "is_first_time_open"
    }

    override fun clearDatabase() {

        val isFirstTime = sharedPreferences.getBoolean(IS_FIRST_TIME_OPEN, true)
        if (isFirstTime) {
            sharedPreferences.edit().putBoolean(IS_FIRST_TIME_OPEN, false).apply()
            appDatabase?.clearAllTables()
        }


    }

    override fun convertAndSave(data: ConversionRatesResult) {

        appDatabase?.let { database ->
            val dao = database.currencyDao()
            val convertedData = data.conversionRates.mapToArray()

            convertedData.forEach { currency ->
                dao.saveCurrency(
                    CurrencyEntity(
                        currencyCode = currency.currencyCode,
                        currencyName = currency.currencyName,
                        currencyRate = currency.relativeValue,
                        flagId = currency.flagId
                    )
                )
            }
        }
    }

    override fun retrieveAndConvert(): ConversionRatesResult {

        appDatabase?.let { database ->
            val dao = database.currencyDao()
            val convertedData =
                convertEntity(dao.retrieveCurrencies()).associateBy { it.currencyCode }

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
                        relativeValue = it.currencyRate,
                        flagId = it.flagId
                    )
                )
            }
        }
    }
}