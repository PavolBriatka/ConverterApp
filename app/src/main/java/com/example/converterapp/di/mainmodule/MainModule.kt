package com.example.converterapp.di.mainmodule

import android.content.Context
import com.example.converterapp.repository.conversionratesrepo.ConversionRatesRepo
import com.example.converterapp.repository.conversionratesrepo.IConversionRatesRepo
import com.example.converterapp.ui.main.adapter.ConverterAdapter
import com.example.converterapp.utils.currencyhelper.CurrencyHelper
import com.example.converterapp.utils.currencyhelper.ICurrencyHelper
import com.example.converterapp.utils.databaseutil.IDatabaseUtil
import com.example.converterapp.webservice.EndpointDefinition
import com.example.converterapp.webservice.conversionratesinteractor.ConversionRatesInteractor
import com.example.converterapp.webservice.conversionratesinteractor.IConversionRatesInteractor
import dagger.Module
import dagger.Provides

@Module
class MainModule {

    @MainScope
    @Provides
    fun provideCurrencyHelper(context: Context): ICurrencyHelper {
        return CurrencyHelper(context)
    }

    @MainScope
    @Provides
    fun provideCurrencyAdapter(): ConverterAdapter {
        return ConverterAdapter()
    }

    @MainScope
    @Provides
    fun provideConversionRatesRepo(
        interactor: IConversionRatesInteractor,
        currencyHelper: ICurrencyHelper,
        databaseUtil: IDatabaseUtil
    ): IConversionRatesRepo {
        return ConversionRatesRepo(interactor, currencyHelper, databaseUtil)
    }

    @MainScope
    @Provides
    fun provideConversionRatesInteractor(api: EndpointDefinition): IConversionRatesInteractor {
        return ConversionRatesInteractor(api)
    }
}