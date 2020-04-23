package com.example.converterapp.webservice.conversionratesinteractor

import io.reactivex.Observable
import retrofit2.Response

class ConversionRatesInteractor:
    IConversionRatesInteractor {

    override fun fetchConversionRates(): Observable<Response<ConversionRatesResponseModel>> {
        TODO("Not yet implemented")
    }
}