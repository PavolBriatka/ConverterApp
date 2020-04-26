package com.example.converterapp.di.mainmodule

import androidx.lifecycle.ViewModel
import com.example.converterapp.di.viewmodelfactory.ViewModelKey
import com.example.converterapp.ui.main.viewmodel.MainViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class MainViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(MainViewModel::class)
    abstract fun provideMainViewModel(viewModel: MainViewModel): ViewModel
}