package com.example.converterapp.ui.main.viewmodel

import androidx.lifecycle.ViewModel
import com.example.converterapp.repository.ResultBase
import com.example.converterapp.repository.conversionratesrepo.ConversionRatesResult.Currency
import com.example.converterapp.repository.conversionratesrepo.IConversionRatesRepo
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.PublishSubject
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class MainViewModel @Inject constructor(private val repository: IConversionRatesRepo) : ViewModel(),
    ViewModelContract {

    private val dataSubject: PublishSubject<Map<String, Currency>> = PublishSubject.create()
    private val disposables = CompositeDisposable()

    override fun fetchCurrencyRates() {
        Observable.interval(0, 1, TimeUnit.SECONDS)
            .flatMap {
                repository.fetchConversionRates()
                    .map { result ->
                        when (result) {
                            is ResultBase.Success -> {
                                result.result.conversionRates
                            }
                            else -> mapOf()
                        }
                    }
            }.subscribe(dataSubject::onNext)
            .let { disposables.add(it) }
    }

    override fun getCurrencyData(): Observable<Map<String, Currency>> {
        return dataSubject.hide()
    }
}