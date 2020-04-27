package com.example.converterapp.ui.main

import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.converterapp.R
import com.example.converterapp.di.viewmodelfactory.ViewModelsProviderFactory
import com.example.converterapp.repository.conversionratesrepo.ConversionRatesResult.Currency
import com.example.converterapp.ui.main.adapter.ConverterAdapter
import com.example.converterapp.ui.main.adapter.ConverterAdapterVolTwo
import com.example.converterapp.ui.main.viewmodel.MainViewModel
import com.example.converterapp.utils.mapToArray
import dagger.android.support.DaggerAppCompatActivity
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

class MainActivity : DaggerAppCompatActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelsProviderFactory

    private lateinit var viewModel: MainViewModel
    private val disposables = CompositeDisposable()
    private val converterAdapter: ConverterAdapterVolTwo by lazy { ConverterAdapterVolTwo() }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel = ViewModelProvider(this, viewModelFactory).get(MainViewModel::class.java)
        viewModel.fetchCurrencyRates()

        rv_currency_list.apply {

            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(this@MainActivity)
            converterAdapter.setViewModel(viewModel)
            converterAdapter.onItemClicked = { currency ->
                this.scrollToPosition(0)
            }

        }
    }

    override fun onStart() {
        super.onStart()
        disposables.addAll(subscribeToCurrencyRates())
    }

    private fun subscribeToCurrencyRates(): Disposable {
        return viewModel.getCurrencyData()
            .observeOn(AndroidSchedulers.mainThread())
            .firstElement()
            .subscribe { data ->
                converterAdapter.setData(data.mapToArray())
                rv_currency_list.adapter = converterAdapter
            }

    }
}
