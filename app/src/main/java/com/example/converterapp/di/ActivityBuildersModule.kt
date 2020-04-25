package com.example.converterapp.di

import com.example.converterapp.MainActivity
import com.example.converterapp.di.mainmodule.MainInteractorModule
import com.example.converterapp.di.mainmodule.MainRepositoryModule
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBuildersModule {

    @ContributesAndroidInjector(
        modules = [MainInteractorModule::class,
        MainRepositoryModule::class]
    )
    abstract fun contributeMainActivity(): MainActivity
}