package com.example.converterapp.ui.main

import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.converterapp.R
import com.example.converterapp.di.viewmodelfactory.ViewModelsProviderFactory
import com.example.converterapp.ui.main.adapter.ConverterAdapter
import com.example.converterapp.ui.main.viewmodel.MainViewModel
import com.example.converterapp.utils.mapToArray
import dagger.android.support.DaggerAppCompatActivity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

class MainActivity : DaggerAppCompatActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelsProviderFactory
    @Inject
    lateinit var converterAdapter: ConverterAdapter

    private lateinit var viewModel: MainViewModel
    private val disposables = CompositeDisposable()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupToolbar()

        viewModel = ViewModelProvider(this, viewModelFactory).get(MainViewModel::class.java)

        rv_currency_list.apply {

            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(this@MainActivity)
            converterAdapter.setViewModel(viewModel)
            converterAdapter.onItemClicked = {
                this.scrollToPosition(0)
            }

        }
    }

    private fun setupToolbar() {
        supportActionBar.apply {
            this?.elevation = 0f
            this?.title = getString(R.string.main_screen_title)
        }
    }

    override fun onStart() {
        super.onStart()
        viewModel.fetchCurrencyRates()
        disposables.add(loadCurrencyRates())
    }

    override fun onStop() {
        disposables.clear()
        viewModel.clearSubscriptions()
        super.onStop()
    }

    private fun loadCurrencyRates(): Disposable {
        return viewModel.getCurrencyData()
            .observeOn(AndroidSchedulers.mainThread())
            .firstElement()
            .subscribe { data ->
                converterAdapter.setData(data.mapToArray())
                rv_currency_list.adapter = converterAdapter
            }

    }
}
