package com.example.converterapp

import com.example.converterapp.di.DaggerTestAppComponent
import com.example.converterapp.repository.FakeConversionRatesRepo
import dagger.android.AndroidInjector
import dagger.android.support.DaggerApplication

 open class TestBaseApplication : DaggerApplication() {

    val fakeRepo = FakeConversionRatesRepo()

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        return DaggerTestAppComponent.builder().application(this).build()
    }
}