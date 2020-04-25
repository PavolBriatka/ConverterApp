package com.example.converterapp.di.mainmodule

import com.example.converterapp.webservice.EndpointDefinition
import com.example.converterapp.webservice.conversionratesinteractor.ConversionRatesInteractor
import com.example.converterapp.webservice.conversionratesinteractor.IConversionRatesInteractor
import dagger.Module
import dagger.Provides

@Module
class MainModule {


    @Provides
    fun provideCOnversionRatesInteractor(api: EndpointDefinition): IConversionRatesInteractor {
        return ConversionRatesInteractor(api)
    }
}