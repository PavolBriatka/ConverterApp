package com.example.converterapp.ui.main

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.converterapp.R
import com.example.converterapp.di.viewmodelfactory.ViewModelsProviderFactory
import com.example.converterapp.ui.ConnectivityObservable
import com.example.converterapp.ui.IConnectivityObervable
import com.example.converterapp.ui.main.adapter.ConverterAdapter
import com.example.converterapp.ui.main.viewmodel.MainViewModel
import com.example.converterapp.utils.mapToArray
import com.google.android.material.snackbar.Snackbar
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

    @Inject
    lateinit var connectivityObservable: IConnectivityObervable

    private lateinit var viewModel: MainViewModel
    private lateinit var snackbar: Snackbar
    private val disposables = CompositeDisposable()


    override fun onCreate(savedInstanceState: Bundle?) {

        setTheme(R.style.AppTheme)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupToolbar()
        setupSnackbar()

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

    private fun setupSnackbar() {
        snackbar = Snackbar.make(
            cl_main_activity,
            getString(R.string.response_error_message),
            5000
        )
        val view = snackbar.view
        view.setBackgroundColor(ContextCompat.getColor(this, R.color.snackbarBackgorund))
    }

    private fun setupToolbar() {
        supportActionBar.apply {
            this?.elevation = 0f
            this?.title = getString(R.string.main_screen_title)
        }
    }

    override fun onStart() {
        super.onStart()
        connectivityObservable.registerNetworkStateObserver()
        disposables.addAll(
            loadCurrencyRates(),
            observeErrorNotification(),
            observeNetworkStatus()
        )
    }

    override fun onStop() {
        disposables.clear()
        viewModel.clearSubscriptions()
        connectivityObservable.unregisterNetworkStateObserver()
        super.onStop()
    }

    private fun observeNetworkStatus(): Disposable {
        return connectivityObservable.networkState()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { isAvailable ->

                viewModel.clearSubscriptions()
                viewModel.fetchCurrencyRates(isAvailable)

                if (!isAvailable) snackbar.show()
            }
    }

    private fun loadCurrencyRates(): Disposable {
        return viewModel.getCurrencyData()
            .observeOn(AndroidSchedulers.mainThread())
            .filter { it.isNotEmpty() }
            .firstElement()
            .subscribe { data ->
                ll_no_data_screen.visibility = View.GONE
                converterAdapter.setData(data.mapToArray())
                rv_currency_list.adapter = converterAdapter

            }

    }

    private fun observeErrorNotification(): Disposable {
        return viewModel.getErrorNotification()
            .observeOn(AndroidSchedulers.mainThread())
            .filter { it }
            .subscribe {
                ll_no_data_screen.visibility =
                    if (converterAdapter.hasData()) View.GONE else View.VISIBLE
                snackbar.show()
            }
    }
}
