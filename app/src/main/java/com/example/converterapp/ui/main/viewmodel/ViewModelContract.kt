package com.example.converterapp.ui.main.viewmodel

import com.example.converterapp.repository.conversionratesrepo.ConversionRatesResult.Currency
import io.reactivex.Observable

interface ViewModelContract {

    fun fetchCurrencyRates(isNetworkAvailable: Boolean)
    fun getCurrencyData(): Observable<Map<String, Currency>>
    fun getUserInput(): Observable<Pair<String, String>>
    fun updateUserInput(input: Pair<String, String>)
    fun getErrorNotification(): Observable<Boolean>
    fun clearSubscriptions()
}