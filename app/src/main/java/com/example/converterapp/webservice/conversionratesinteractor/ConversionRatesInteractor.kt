package com.example.converterapp.webservice.conversionratesinteractor

import com.example.converterapp.webservice.EndpointDefinition
import io.reactivex.Observable
import io.reactivex.Single
import retrofit2.Response
import javax.inject.Inject

class ConversionRatesInteractor @Inject constructor(private val api: EndpointDefinition) : IConversionRatesInteractor {

    override fun fetchConversionRates(baseCurrency: String): Single<Response<ConversionRatesResponseModel>> {

        return api.fetchConversionRates(baseCurrency)
    }
}