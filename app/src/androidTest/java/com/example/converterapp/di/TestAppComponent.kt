package com.example.converterapp.di

import android.app.Application
import com.example.converterapp.TestBaseApplication
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(
    modules = [AndroidSupportInjectionModule::class,
        TestActivityBuildersModule::class,
        TestMainViewModelModule::class,
        TestAppModule::class]
)
interface TestAppComponent : AndroidInjector<TestBaseApplication> {


    @Component.Builder
    interface Builder {

        @BindsInstance
        fun application(application: Application): Builder

        fun build(): TestAppComponent
    }
}