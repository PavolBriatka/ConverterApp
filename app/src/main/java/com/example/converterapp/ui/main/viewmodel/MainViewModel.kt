package com.example.converterapp.ui.main.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.converterapp.repository.ResultBase
import com.example.converterapp.repository.conversionratesrepo.ConversionRatesResult.Currency
import com.example.converterapp.repository.conversionratesrepo.IConversionRatesRepo
import com.example.converterapp.utils.round
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.BiFunction
import io.reactivex.subjects.BehaviorSubject
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class MainViewModel @Inject constructor(private val repository: IConversionRatesRepo) : ViewModel(),
    ViewModelContract {

    private val ratesSubject: BehaviorSubject<Map<String, Currency>> = BehaviorSubject.create()
    private val userInputSubject: BehaviorSubject<Pair<String, String>> =
        BehaviorSubject.createDefault(Pair("EUR", "1"))
    private val disposables = CompositeDisposable()

    override fun fetchCurrencyRates() {
        Observable.interval(0, 1, TimeUnit.SECONDS)
            .flatMap {
                repository.fetchConversionRates()
                    .flatMapSingle { result ->
                        Log.e("result", "called")
                        when (result) {
                            is ResultBase.Success -> {
                                Single.just(result.result.conversionRates)
                            }
                            else -> Single.just(mapOf())
                        }
                    }.take(3)
            }
            .subscribe(ratesSubject::onNext)
            .let { disposables.add(it) }
    }

    override fun getCurrencyData(): Observable<Map<String, Currency>> {
        return Observable.combineLatest(getCurrencyRates(), getUserInput(),
            BiFunction<Map<String, Currency>, Pair<String, String>, Map<String, Currency>> { ratesMap, baseCurrency ->
                val baseCurrencyRate = ratesMap[baseCurrency.first]?.relativeRate!!
                val userInput = baseCurrency.second
                val finalData = mutableMapOf<String, Currency>()
                for ((code, currency) in ratesMap) {
                    finalData[code] =
                        currency.copy(relativeRate = ((currency.relativeRate / baseCurrencyRate) * userInput.toDouble()).round(3))
                }
                finalData
            })
    }

    private fun getCurrencyRates(): Observable<Map<String, Currency>> {
        return ratesSubject.hide()
    }

    override fun getUserInput(): Observable<Pair<String, String>> {
        return userInputSubject.hide()
    }

    override fun updateUserInput(input: Pair<String, String>) {
        Log.e("userInput", "${input.first}, ${input.second}")
        userInputSubject.onNext(input)
    }
}