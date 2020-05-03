package com.example.converterapp.repository

import com.example.converterapp.R
import com.example.converterapp.repository.conversionratesrepo.ConversionRatesResult
import com.example.converterapp.repository.conversionratesrepo.ConversionRatesResult.*
import com.example.converterapp.repository.conversionratesrepo.IConversionRatesRepo
import io.reactivex.Observable

class FakeConversionRatesRepo : IConversionRatesRepo {

    var isSuccess = false

    override fun fetchConversionRates(
        baseCurrency: String,
        isNetworkAvailable: Boolean
    ): Observable<ResultBase<ConversionRatesResult>> {
        return if (isSuccess) {
            Observable.just(ResultBase.Success(
                ConversionRatesResult(
                    conversionRates = mapOf(
                        "EUR" to Currency("EUR", "Euro", 1.0, R.drawable.flag_eu),
                        "AUD" to Currency("AUD", "Australian Dollar", 2.0, R.drawable.flag_australia),
                        "CAD" to Currency("CAD", "Canadian Dollar", 3.0, R.drawable.flag_canada),
                        "GBP" to Currency("GBP", "British Pound", 4.0, R.drawable.flag_united_kingdom),
                        "USD" to Currency("USD", "American Dollar", 5.0, R.drawable.flag_usa)
                    )
                )))
        } else {
            Observable.just(ResultBase.Success(
                ConversionRatesResult(
                    conversionRates = mapOf(
                        "EUR" to Currency("EUR", "Euro", 1.0, R.drawable.flag_eu),
                        "AUD" to Currency("AUD", "Australian Dollar", 2.0, R.drawable.flag_australia),
                        "CAD" to Currency("CAD", "Canadian Dollar", 3.0, R.drawable.flag_canada),
                        "GBP" to Currency("GBP", "British Pound", 4.0, R.drawable.flag_united_kingdom),
                        "USD" to Currency("USD", "American Dollar", 6.0, R.drawable.flag_usa)
                    )
                )))
        }
    }
}