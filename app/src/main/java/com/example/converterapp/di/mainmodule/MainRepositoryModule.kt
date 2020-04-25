package com.example.converterapp.di.mainmodule

import com.example.converterapp.repository.conversionratesrepo.ConversionRatesRepo
import com.example.converterapp.repository.conversionratesrepo.IConversionRatesRepo
import com.example.converterapp.webservice.conversionratesinteractor.IConversionRatesInteractor
import dagger.Module
import dagger.Provides

@Module
class MainRepositoryModule {

    @Provides
    fun provideConversionRatesRepo(interactor: IConversionRatesInteractor): IConversionRatesRepo {
        return ConversionRatesRepo(interactor)
    }
}