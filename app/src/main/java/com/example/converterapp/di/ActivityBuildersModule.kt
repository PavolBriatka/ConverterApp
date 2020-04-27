package com.example.converterapp.di

import com.example.converterapp.di.mainmodule.MainInteractorModule
import com.example.converterapp.di.mainmodule.MainRepositoryModule
import com.example.converterapp.di.mainmodule.MainViewModelModule
import com.example.converterapp.ui.main.MainActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBuildersModule {

    @ContributesAndroidInjector(
        modules = [MainInteractorModule::class,
        MainRepositoryModule::class,
        MainViewModelModule::class]
    )
    abstract fun contributeMainActivity(): MainActivity
}