package com.example.converterapp.di

import com.example.converterapp.di.mainmodule.MainModule
import com.example.converterapp.di.mainmodule.MainScope
import com.example.converterapp.di.mainmodule.MainViewModelModule
import com.example.converterapp.ui.main.MainActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBuildersModule {

    @MainScope
    @ContributesAndroidInjector(
        modules = [MainModule::class,
            MainViewModelModule::class]
    )
    abstract fun contributeMainActivity(): MainActivity
}