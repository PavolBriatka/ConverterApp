package com.example.converterapp.mocks

import com.example.converterapp.R
import com.example.converterapp.mocks.FakeConversionRatesRepo.Scenario.*
import com.example.converterapp.repository.ResultBase
import com.example.converterapp.repository.ResultBase.ErrorType.*
import com.example.converterapp.repository.conversionratesrepo.ConversionRatesResult
import com.example.converterapp.repository.conversionratesrepo.ConversionRatesResult.*
import com.example.converterapp.repository.conversionratesrepo.IConversionRatesRepo
import io.reactivex.Observable

class FakeConversionRatesRepo : IConversionRatesRepo {

    var scenario: Scenario = DEFAULT

    override fun fetchConversionRates(
        baseCurrency: String,
        isNetworkAvailable: Boolean
    ): Observable<ResultBase<ConversionRatesResult>> {

        return when (scenario) {
            EMPTY_DB_NO_INTERNET -> {
                Observable.just(
                    ResultBase.Error(
                        DATABASE_ERROR
                    )
                )
            }
            EMPTY_DB_INTERNET_NETWORK_ERROR -> {
                Observable.just(
                    ResultBase.Error(
                        DATABASE_ERROR
                    ),
                    ResultBase.Error(
                        NETWORK_ERROR
                    )
                )
            }
            EMPTY_DB_INTERNET_SUCCESS -> {
                Observable.just(
                    ResultBase.Error(
                        DATABASE_ERROR
                    ), networkData)
            }
            DB_SUCCESS_NO_INTERNET -> {
                Observable.just(databaseData)
            }
            DB_SUCCESS_INTERNET_NETWORK_ERROR -> {
                Observable.just(databaseData,
                    ResultBase.Error(
                        NETWORK_ERROR
                    )
                )
            }
            else -> {
                Observable.just(databaseData, networkData)
            }
        }
    }

    enum class Scenario {
        DEFAULT,
        EMPTY_DB_NO_INTERNET,
        EMPTY_DB_INTERNET_NETWORK_ERROR,
        EMPTY_DB_INTERNET_SUCCESS,
        DB_SUCCESS_NO_INTERNET,
        DB_SUCCESS_INTERNET_NETWORK_ERROR,
        DB_SUCCESS_INTERNET_SUCCESS
    }

    private val networkData =
        ResultBase.Success(
            ConversionRatesResult(
                conversionRates = mapOf(
                    "EUR" to Currency("EUR", "Euro", 1.0, R.drawable.flag_eu),
                    "AUD" to Currency("AUD", "Australian Dollar", 2.0, R.drawable.flag_australia),
                    "CAD" to Currency("CAD", "Canadian Dollar", 3.0, R.drawable.flag_canada),
                    "GBP" to Currency("GBP", "British Pound", 4.0, R.drawable.flag_united_kingdom),
                    "USD" to Currency("USD", "American Dollar", 5.0, R.drawable.flag_usa)
                )
            )
        )

    private val databaseData =
        ResultBase.Success(
            ConversionRatesResult(
                conversionRates = mapOf(
                    "EUR" to Currency("EUR", "Euro", 1.0, R.drawable.flag_eu),
                    "AUD" to Currency("AUD", "Australian Dollar", 3.0, R.drawable.flag_australia),
                    "CAD" to Currency("CAD", "Canadian Dollar", 4.0, R.drawable.flag_canada),
                    "GBP" to Currency("GBP", "British Pound", 5.0, R.drawable.flag_united_kingdom),
                    "USD" to Currency("USD", "American Dollar", 6.0, R.drawable.flag_usa)
                )
            )
        )
}