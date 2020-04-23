package com.example.converterapp.webservice

import com.example.converterapp.webservice.conversionratesinteractor.ConversionRatesResponseModel
import io.reactivex.Observable
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface EndpointDefinition {

    @GET("latest")
    fun fetchCurrencyRates(@Query("base") base: String = "EUR") : Observable<Response<ConversionRatesResponseModel>>
}