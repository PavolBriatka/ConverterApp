package com.example.converterapp

import com.example.converterapp.di.DaggerTestAppComponent
import com.example.converterapp.repository.FakeConversionRatesRepo
import com.example.converterapp.ui.FakeConnectivityObservable
import dagger.android.AndroidInjector
import dagger.android.support.DaggerApplication

open class TestBaseApplication : DaggerApplication() {

    val fakeConversionRatesRepo = FakeConversionRatesRepo()
    val fakeConnectivityObservable = FakeConnectivityObservable()

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        return DaggerTestAppComponent.builder().application(this).build()
    }
}