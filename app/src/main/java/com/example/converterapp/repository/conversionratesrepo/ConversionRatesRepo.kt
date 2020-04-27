package com.example.converterapp.repository.conversionratesrepo

import com.example.converterapp.repository.ResultBase
import com.example.converterapp.repository.conversionratesrepo.ConversionRatesResult.Currency
import com.example.converterapp.utils.CurrencyHelper
import com.example.converterapp.webservice.conversionratesinteractor.ConversionRatesResponseModel
import com.example.converterapp.webservice.conversionratesinteractor.IConversionRatesInteractor
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class ConversionRatesRepo @Inject constructor(
    private val interactor: IConversionRatesInteractor,
    private val currencyHelper: CurrencyHelper
) :
    IConversionRatesRepo {

    override fun fetchConversionRates(baseCurrency: String): Observable<out ResultBase<ConversionRatesResult>> {
        return interactor.fetchConversionRates(baseCurrency)
            .subscribeOn(Schedulers.io())
            .map { response ->
                when (response.code()) {
                    200 -> handleResponseSuccess(response.body())
                    else -> ResultBase.Error
                }
            }
            .onErrorReturn {
                ResultBase.Error
            }
    }

    private fun handleResponseSuccess(responseBody: ConversionRatesResponseModel?): ResultBase<ConversionRatesResult> {
        responseBody?.let { data ->
            val ratesArray = assembleData(data)
            return when {
                ratesArray.isNotEmpty() -> ResultBase.Success(ConversionRatesResult(ratesArray))
                else -> ResultBase.Error
            }
        } ?: return ResultBase.Error
    }

    private fun assembleData(data: ConversionRatesResponseModel): MutableMap<String, Currency> {

        val currencyRates = mutableMapOf<String, Currency>()
        data.baseCurrency?.let {
            data.rates?.let {
                //1. Add base currency
                currencyRates[data.baseCurrency] = Currency(
                    currencyCode = data.baseCurrency,
                    currencyName = currencyHelper.currencyMap[data.baseCurrency]?.first!!,
                    flagId = currencyHelper.currencyMap[data.baseCurrency]?.second!!,
                    relativeRate = 1.0
                )
                //2. Add rest of the currencies
                for ((code, rate) in data.rates) {
                    currencyRates[code] = Currency(
                        currencyCode = code,
                        currencyName = currencyHelper.currencyMap[code]?.first!!,
                        flagId = currencyHelper.currencyMap[code]?.second!!,
                        relativeRate = rate
                    )
                }
            }
        }
        return currencyRates
    }
}