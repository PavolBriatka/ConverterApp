package com.example.converterapp.ui.main.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.converterapp.repository.ResultBase
import com.example.converterapp.repository.conversionratesrepo.ConversionRatesResult.Currency
import com.example.converterapp.repository.conversionratesrepo.IConversionRatesRepo
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.BehaviorSubject
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class MainViewModel @Inject constructor(private val repository: IConversionRatesRepo) : ViewModel(),
    ViewModelContract {

    private val dataSubject: BehaviorSubject<Map<String, Currency>> = BehaviorSubject.create()
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
            }
            .subscribe{
                Log.e("size", "${it.size}")
                dataSubject.onNext(it)
            }
            .let { disposables.add(it) }
    }

    override fun getCurrencyData(): Observable<Map<String, Currency>> {
        return dataSubject.hide()
    }
}