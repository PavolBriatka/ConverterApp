package com.example.converterapp.di

import android.app.Application
import com.example.converterapp.TestBaseApplication
import com.example.converterapp.repository.conversionratesrepo.IConversionRatesRepo
import com.example.converterapp.ui.IConnectivityObervable
import com.example.converterapp.ui.main.adapter.ConverterAdapter
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class TestAppModule {

    @Singleton
    @Provides
    fun provideConnectivityObservable(application: Application): IConnectivityObervable {
        return (application as TestBaseApplication).fakeConnectivityObservable
    }

    @Singleton
    @Provides
    fun provideCurrencyAdapter(): ConverterAdapter {
        return ConverterAdapter()
    }

    @Singleton
    @Provides
    fun provideConversionRatesRepo(application: Application): IConversionRatesRepo {
        return (application as TestBaseApplication).fakeConversionRatesRepo
    }
}