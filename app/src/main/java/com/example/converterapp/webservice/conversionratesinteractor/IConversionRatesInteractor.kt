package com.example.converterapp.webservice.conversionratesinteractor

import com.example.converterapp.webservice.conversionratesinteractor.ConversionRatesResponseModel
import io.reactivex.Observable
import retrofit2.Response

interface IConversionRatesInteractor {

    fun fetchConversionRates(): Observable<Response<ConversionRatesResponseModel>>
}