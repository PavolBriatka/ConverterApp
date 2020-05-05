package com.example.converterapp.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.converterapp.di.viewmodelfactory.ViewModelKey
import com.example.converterapp.ui.main.viewmodel.MainViewModel
import com.example.converterapp.mocks.FakeViewModelsProviderFactory
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class TestMainViewModelModule {

    @Binds
    abstract fun bindViewModelFactory(viewModelFactory: FakeViewModelsProviderFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(MainViewModel::class)
    abstract fun provideMainViewModel(viewModel: MainViewModel): ViewModel
}