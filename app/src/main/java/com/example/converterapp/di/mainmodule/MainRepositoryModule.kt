package com.example.converterapp.di.mainmodule

import android.app.Application
import android.content.res.Resources
import com.example.converterapp.repository.conversionratesrepo.ConversionRatesRepo
import com.example.converterapp.repository.conversionratesrepo.IConversionRatesRepo
import com.example.converterapp.utils.CurrencyHelper
import com.example.converterapp.webservice.conversionratesinteractor.IConversionRatesInteractor
import dagger.Module
import dagger.Provides

@Module
class MainRepositoryModule {

    @Provides
    fun provideContext(application: Application): Resources {
        return application.resources
    }

    @Provides
    fun provideCurrencyHelper(resources: Resources): CurrencyHelper {
        return CurrencyHelper(resources)
    }

    @Provides
    fun provideConversionRatesRepo(interactor: IConversionRatesInteractor, currencyHelper: CurrencyHelper): IConversionRatesRepo {
        return ConversionRatesRepo(interactor, currencyHelper)
    }
}