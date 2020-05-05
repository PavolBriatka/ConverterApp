package com.example.converterapp.repository.conversionratesrepo

import com.example.converterapp.repository.ResultBase
import com.example.converterapp.repository.ResultBase.ErrorType.*
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

        /**
         * This action is actually performed only if the user:
         * 1. installs and opens the app for the very first time
         * 2. uninstalls the app and opens it again
         * 3. clears data manually in App Manager on the device
         * This serves to clear any previously stored arbitrary currency flag IDs that might have been
         * restored (on SDK 23 and up data backup happens automatically) after the user reinstalled
         * the app. Reinstalling causes that currency flag drawables are assigned new arbitrary Int
         * values that are not the same as the previous ones which caused UI inconsistencies.
         * */
        databaseUtil.clearDatabase()

        return if (isNetworkAvailable) {
            currencyRatesDod.observe(baseCurrency).extractData()
        } else {
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
                else -> ResultBase.Error(DATABASE_ERROR)
            }
        }.subscribeOn(Schedulers.io())

    }

    private fun handleResponseSuccess(responseBody: ConversionRatesResponseModel?): ResultBase<ConversionRatesResult> {
        responseBody?.let { data ->
            val ratesArray = assembleData(data)
            return when {
                ratesArray.isNotEmpty() -> ResultBase.Success(ConversionRatesResult(ratesArray))
                else -> ResultBase.Error(NETWORK_ERROR)
            }
        } ?: return ResultBase.Error(NETWORK_ERROR)
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
                    relativeValue = 1.0
                )
                //2. Add rest of the currencies
                for ((code, rate) in data.rates) {
                    val currencyRes = currencyHelper.fetchResources(code)
                    currencyRates[code] = Currency(
                        currencyCode = code,
                        currencyName = currencyRes?.first ?: "",
                        flagId = currencyRes?.second ?: -1,
                        relativeValue = rate
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
                            else -> ResultBase.Error(NETWORK_ERROR)
                        }
                    }
                    .onErrorReturn {
                        ResultBase.Error(NETWORK_ERROR)
                    }
            },
            fromStorage = {_, _ ->
                val dbData = databaseUtil.retrieveAndConvert()
                when {
                    dbData.conversionRates.isNotEmpty() -> {
                        ResultBase.Success(dbData)
                    }
                    else -> ResultBase.Error(DATABASE_ERROR)
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