package com.example.converterapp.repository.conversionratesrepo

import com.example.converterapp.repository.ResultBase
import com.revolut.rxdata.dod.Data
import io.reactivex.Observable

interface IConversionRatesRepo {

    fun fetchConversionRates(baseCurrency: String = "EUR"): Observable<ResultBase<ConversionRatesResult>>
}