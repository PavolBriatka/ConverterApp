package com.example.converterapp.repository.interactor

import io.reactivex.Observable
import retrofit2.Response

interface IConversionRatesInteractor {

    fun fetchConversionRates(): Observable<Response<ConversionRatesResponseModel>>
}