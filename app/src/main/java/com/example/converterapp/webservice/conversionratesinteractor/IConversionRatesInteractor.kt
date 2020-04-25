package com.example.converterapp.webservice.conversionratesinteractor

import io.reactivex.Observable
import retrofit2.Response

interface IConversionRatesInteractor {

    fun fetchConversionRates(baseCurrency: String): Observable<Response<ConversionRatesResponseModel>>
}