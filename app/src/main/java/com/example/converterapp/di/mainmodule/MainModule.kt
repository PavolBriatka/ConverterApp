package com.example.converterapp.di.mainmodule

import android.content.Context
import com.example.converterapp.database.AppDatabase
import com.example.converterapp.repository.conversionratesrepo.ConversionRatesRepo
import com.example.converterapp.repository.conversionratesrepo.IConversionRatesRepo
import com.example.converterapp.ui.main.adapter.ConverterAdapter
import com.example.converterapp.utils.CurrencyHelper
import com.example.converterapp.utils.DatabaseUtil
import com.example.converterapp.webservice.EndpointDefinition
import com.example.converterapp.webservice.conversionratesinteractor.ConversionRatesInteractor
import com.example.converterapp.webservice.conversionratesinteractor.IConversionRatesInteractor
import dagger.Module
import dagger.Provides
import retrofit2.Converter

@Module
class MainModule {

    @MainScope
    @Provides
    fun provideCurrencyHelper(context: Context): CurrencyHelper {
        return CurrencyHelper(context)
    }


    @MainScope
    @Provides
    fun provideCurrencyAdapter(): ConverterAdapter {
        return  ConverterAdapter()
    }

    @MainScope
    @Provides
    fun provideConversionRatesRepo(
        interactor: IConversionRatesInteractor,
        currencyHelper: CurrencyHelper,
        databaseUtil: DatabaseUtil
    ): IConversionRatesRepo {
        return ConversionRatesRepo(interactor, currencyHelper, databaseUtil)
    }

    @MainScope
    @Provides
    fun provideConversionRatesInteractor(api: EndpointDefinition): IConversionRatesInteractor {
        return ConversionRatesInteractor(api)
    }
}