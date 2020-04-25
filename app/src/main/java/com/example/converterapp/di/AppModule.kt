package com.example.converterapp.di

import com.example.converterapp.BuildConfig
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