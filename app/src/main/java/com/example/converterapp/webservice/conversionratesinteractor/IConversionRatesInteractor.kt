package com.example.converterapp.webservice.conversionratesinteractor

import io.reactivex.Observable
import io.reactivex.Single
import retrofit2.Response

interface IConversionRatesInteractor {

    fun fetchConversionRates(baseCurrency: String): Single<Response<ConversionRatesResponseModel>>
}