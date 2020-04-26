package com.example.converterapp.ui.main.viewmodel

import androidx.lifecycle.ViewModel
import com.example.converterapp.repository.ResultBase
import com.example.converterapp.repository.conversionratesrepo.ConversionRatesResult.Currency
import com.example.converterapp.repository.conversionratesrepo.IConversionRatesRepo
import io.reactivex.Observable
import javax.inject.Inject

class MainViewModel @Inject constructor(private val repository: IConversionRatesRepo) : ViewModel(),
    ViewModelContract {

    override fun fetchCurrencyRates(): Observable<ArrayList<Currency>> {
        return repository.fetchConversionRates()
            .map { result ->
                when (result) {
                    is ResultBase.Success -> result.result.conversionRates
                    else -> arrayListOf()
                }
            }
    }
}