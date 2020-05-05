package com.example.converterapp.di

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import com.example.converterapp.BuildConfig
import com.example.converterapp.database.AppDatabase
import com.example.converterapp.ui.ConnectivityObservable
import com.example.converterapp.ui.IConnectivityObervable
import com.example.converterapp.utils.databaseutil.DatabaseUtil
import com.example.converterapp.utils.databaseutil.IDatabaseUtil
import com.example.converterapp.webservice.EndpointDefinition
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
class AppModule {

    companion object {
        const val BASE_URL = "https://hiring.revolut.codes/api/android/"
        const val APP_PREFERENCES = "appSharedPreferences"
    }

    @Singleton
    @Provides
    fun provideContext(application: Application): Context {
        return application.applicationContext
    }

    @Singleton
    @Provides
    fun provideConnectivityObservable(application: Application): IConnectivityObervable {
        return ConnectivityObservable(application)
    }

    @Singleton
    @Provides
    fun provideAppDatabase(context: Context): AppDatabase? {
        return AppDatabase.getDatabase(context)
    }

    @Singleton
    @Provides
    fun provideSharedPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE)
    }

    @Singleton
    @Provides
    fun provideDatabaseUtil(appDatabase: AppDatabase?, sharedPreferences: SharedPreferences): IDatabaseUtil {
        return DatabaseUtil(appDatabase, sharedPreferences)
    }

    @Singleton
    @Provides
    fun provideOkHttpClientBuilder(): OkHttpClient.Builder {
        return OkHttpClient.Builder()
            .readTimeout(30, TimeUnit.SECONDS)
            .connectTimeout(30, TimeUnit.SECONDS)
    }

    @Singleton
    @Provides
    fun provideOkHttpClient(builder: OkHttpClient.Builder): OkHttpClient {
        return if (BuildConfig.DEBUG) {
            val interceptor =
                HttpLoggingInterceptor().apply {
                    level = HttpLoggingInterceptor.Level.BODY
                }

            builder.addInterceptor(interceptor).build()
        } else {
            builder.build()
        }
    }

    @Singleton
    @Provides
    fun provideRetrofitInstance(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(okHttpClient)
            .baseUrl(BASE_URL)
            .build()
    }

    @Singleton
    @Provides
    fun provideApiInterface(retrofit: Retrofit): EndpointDefinition {

        return retrofit.create(EndpointDefinition::class.java)
    }
}