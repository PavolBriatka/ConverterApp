package com.example.converterapp.mocks

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.converterapp.ui.main.viewmodel.MainViewModel
import javax.inject.Inject

class FakeViewModelsProviderFactory @Inject constructor(
    private val mainRepository: FakeConversionRatesRepo
) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(mainRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}