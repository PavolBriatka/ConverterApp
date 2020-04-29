package com.example.converterapp.di.mainmodule

import android.app.Application
import android.content.Context
import com.example.converterapp.database.AppDatabase
import com.example.converterapp.repository.conversionratesrepo.ConversionRatesRepo
import com.example.converterapp.repository.conversionratesrepo.IConversionRatesRepo
import com.example.converterapp.utils.CurrencyHelper
import com.example.converterapp.utils.DatabaseUtil
import com.example.converterapp.webservice.conversionratesinteractor.IConversionRatesInteractor
import dagger.Module
import dagger.Provides

@Module
class MainRepositoryModule {

    @Provides
    fun provideContext(application: Application): Context {
        return application.applicationContext
    }

    @Provides
    fun provideCurrencyHelper(context: Context): CurrencyHelper {
        return CurrencyHelper(context)
    }

    @Provides
    fun provideAppDatabase(context: Context): AppDatabase? {
        return AppDatabase.getDatabase(context)
    }

    @Provides
    fun provideDatabaseUtil(appDatabase: AppDatabase?): DatabaseUtil {
        return DatabaseUtil(appDatabase)
    }

    @Provides
    fun provideConversionRatesRepo(
        interactor: IConversionRatesInteractor,
        currencyHelper: CurrencyHelper,
        databaseUtil: DatabaseUtil
    ): IConversionRatesRepo {
        return ConversionRatesRepo(interactor, currencyHelper, databaseUtil)
    }
}