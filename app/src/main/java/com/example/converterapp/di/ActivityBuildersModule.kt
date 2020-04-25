package com.example.converterapp.di

import com.example.converterapp.MainActivity
import com.example.converterapp.di.mainmodule.MainModule
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBuildersModule {

    @ContributesAndroidInjector(
        modules = [MainModule::class]
    )
    abstract fun contributeMainActivity(): MainActivity
}