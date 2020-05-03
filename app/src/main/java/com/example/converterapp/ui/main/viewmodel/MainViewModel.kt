package com.example.converterapp.ui.main.viewmodel

import androidx.lifecycle.ViewModel
import com.example.converterapp.repository.ResultBase
import com.example.converterapp.repository.ResultBase.ErrorType.DATABASE_ERROR
import com.example.converterapp.repository.ResultBase.ErrorType.NETWORK_ERROR
import com.example.converterapp.repository.conversionratesrepo.ConversionRatesResult.Currency
import com.example.converterapp.repository.conversionratesrepo.IConversionRatesRepo
import com.example.converterapp.utils.round
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.BiFunction
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class MainViewModel @Inject constructor(private val repository: IConversionRatesRepo) : ViewModel(),
    ViewModelContract {

    private val ratesSubject: BehaviorSubject<Map<String, Currency>> = BehaviorSubject.create()
    private val baseCurrencySubject: BehaviorSubject<Pair<String, String>> =
        BehaviorSubject.createDefault(Pair("EUR", "1"))
    private val dataErrorSubject: PublishSubject<Boolean> = PublishSubject.create()
    private val disposables = CompositeDisposable()

    override fun fetchCurrencyRates(isNetworkAvailable: Boolean) {
        Observable.interval(0, 1, TimeUnit.SECONDS)
            .flatMap {
                repository.fetchConversionRates(isNetworkAvailable = isNetworkAvailable)
                    .doOnNext {
                        if (it is ResultBase.Error) {
                            when (it.errorType) {
                                NETWORK_ERROR -> dataErrorSubject.onNext(true)
                                /*
                                * Take db error into account only if the network is down*/
                                DATABASE_ERROR -> {
                                    if (isNetworkAvailable)
                                        dataErrorSubject.onNext(false)
                                    else
                                        dataErrorSubject.onNext(true)
                                }
                            }
                        }
                    }
                    .map { result ->
                        when (result) {
                            is ResultBase.Success -> {
                                //Log.e("Success", "${result.result.conversionRates.size}")
                                result.result.conversionRates
                            }
                            else -> {
                                //Log.e("ERROR", "EMPTY")
                                mapOf()
                            }
                        }
                    }.take(2)
            }
            .filter { it.isNotEmpty() }
            .subscribe(ratesSubject::onNext)
            .let { disposables.add(it) }
    }

    override fun getCurrencyData(): Observable<Map<String, Currency>> {
        return Observable.combineLatest(getCurrencyRates(), getUserInput(),
            BiFunction<Map<String, Currency>, Pair<String, String>, Map<String, Currency>> { ratesMap, baseCurrency ->

                val baseCurrencyRate = ratesMap[baseCurrency.first]?.relativeRate!!
                val userInput = validateInput(baseCurrency.second)
                val finalData = mutableMapOf<String, Currency>()

                for ((code, currency) in ratesMap) {
                    finalData[code] =
                        currency.copy(
                            relativeRate =
                            ((currency.relativeRate / baseCurrencyRate) * userInput).round(2)
                        )
                }
                finalData
            })
    }

    private fun validateInput(input: String): Double {
        return when {
            input.isBlank() -> 0.0
            else -> input.toDouble()
        }
    }

    private fun getCurrencyRates(): Observable<Map<String, Currency>> {
        return ratesSubject.hide()
    }

    override fun getUserInput(): Observable<Pair<String, String>> {
        return baseCurrencySubject.hide()
    }

    override fun updateUserInput(input: Pair<String, String>) {
        baseCurrencySubject.onNext(input)
    }

    override fun getErrorNotification(): Observable<Boolean> {
        return dataErrorSubject.distinct().hide()
    }

    override fun clearSubscriptions() {
        disposables.clear()
    }
}