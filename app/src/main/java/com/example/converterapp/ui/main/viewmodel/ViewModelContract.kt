package com.example.converterapp.ui.main.viewmodel

import com.example.converterapp.repository.conversionratesrepo.ConversionRatesResult.Currency
import io.reactivex.Observable

interface ViewModelContract {

    fun fetchCurrencyRates()
    fun getCurrencyData(): Observable<Map<String, Currency>>
    fun getUserInput(): Observable<String>
    fun updateUserInput(input: String)
}