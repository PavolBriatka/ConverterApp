package com.example.converterapp.repository.conversionratesrepo

import com.example.converterapp.repository.ResultBase
import com.example.converterapp.repository.conversionratesrepo.ConversionRatesResult.Currency
import com.example.converterapp.webservice.conversionratesinteractor.ConversionRatesResponseModel
import com.example.converterapp.webservice.conversionratesinteractor.IConversionRatesInteractor
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import java.util.*
import javax.inject.Inject

class ConversionRatesRepo @Inject constructor(private val interactor: IConversionRatesInteractor) :
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
            val ratesArray = assembleRatesArray(data)
            return when {
                ratesArray.isNotEmpty() -> ResultBase.Success(ConversionRatesResult(ratesArray))
                else -> ResultBase.Error
            }
        } ?: return ResultBase.Error
    }

    private fun assembleRatesArray(data: ConversionRatesResponseModel): ArrayList<Currency> {

        val currencyRates = arrayListOf<Currency>()
        data.baseCurrency?.let {
            data.rates?.let {
                currencyRates.add(Currency(currencyCode = data.baseCurrency, relativeRate = 1.0))

                for ((code, rate) in data.rates) {
                    currencyRates.add(Currency(currencyCode = code, relativeRate = rate))
                }
            }
        }
        return currencyRates
    }
}