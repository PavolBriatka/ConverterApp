package com.example.converterapp.repository.conversionratesrepo

import com.example.converterapp.repository.ResultBase
import com.example.converterapp.repository.conversionratesrepo.ConversionRatesResult.Currency
import com.example.converterapp.utils.currencyhelper.ICurrencyHelper
import com.example.converterapp.utils.databaseutil.IDatabaseUtil
import com.example.converterapp.utils.extractData
import com.example.converterapp.webservice.conversionratesinteractor.ConversionRatesResponseModel
import com.example.converterapp.webservice.conversionratesinteractor.IConversionRatesInteractor
import com.revolut.rxdata.dod.DataObservableDelegate
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class ConversionRatesRepo @Inject constructor(
    private val interactor: IConversionRatesInteractor,
    private val currencyHelper: ICurrencyHelper,
    private val databaseUtil: IDatabaseUtil
) :
    IConversionRatesRepo {

    override fun fetchConversionRates(baseCurrency: String, isNetworkAvailable: Boolean):
            Observable<ResultBase<ConversionRatesResult>> {
        return if (isNetworkAvailable) {
            //Log.e("Network", "fetch")
            currencyRatesDod.observe(baseCurrency).extractData()
        } else {
            //Log.e("Database", "fetch")
            fetchConversionRatesFromDatabase()
        }
    }


    private fun fetchConversionRatesFromDatabase(): Observable<ResultBase<ConversionRatesResult>> {
        return Observable.fromCallable {
            val dbData = databaseUtil.retrieveAndConvert()
            when {
                dbData.conversionRates.isNotEmpty() -> {
                    ResultBase.Success(dbData)
                }
                else -> ResultBase.Error
            }
        }.subscribeOn(Schedulers.io())

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
                val baseCurrencyRes = currencyHelper.fetchResources(data.baseCurrency)
                currencyRates[data.baseCurrency] = Currency(
                    currencyCode = data.baseCurrency,
                    currencyName = baseCurrencyRes?.first!!,
                    flagId = baseCurrencyRes.second,
                    relativeRate = 1.0
                )
                //2. Add rest of the currencies
                for ((code, rate) in data.rates) {
                    val currencyRes = currencyHelper.fetchResources(code)
                    currencyRates[code] = Currency(
                        currencyCode = code,
                        currencyName = currencyRes?.first!!,
                        flagId = currencyRes.second,
                        relativeRate = rate
                    )
                }
            }
        }
        return currencyRates
    }

    private val currencyRatesDod: DataObservableDelegate<String, String, ResultBase<ConversionRatesResult>> =
        DataObservableDelegate(
            paramsKey = { "CURRENCY_RATES" },
            fromNetwork = { baseCurrency ->
                interactor.fetchConversionRates(baseCurrency)
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
            },
            fromStorage = {_, _ ->
                val dbData = databaseUtil.retrieveAndConvert()
                when {
                    dbData.conversionRates.isNotEmpty() -> {
                        ResultBase.Success(dbData)
                    }
                    else -> ResultBase.Error
                }
            },
            toStorage = { _, _, domain ->
                when (domain) {
                    is ResultBase.Success -> {
                        databaseUtil.convertAndSave(domain.result)
                    }
                }
            }

        )
}