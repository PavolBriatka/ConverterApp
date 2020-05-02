package com.example.converterapp.repository.conversionratesrepo

import com.example.converterapp.repository.ResultBase
import io.reactivex.Observable

interface IConversionRatesRepo {

    fun fetchConversionRates(baseCurrency: String = "EUR", isNetworkAvailable: Boolean):
            Observable<ResultBase<ConversionRatesResult>>
}