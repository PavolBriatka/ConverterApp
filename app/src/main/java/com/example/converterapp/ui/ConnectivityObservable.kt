package com.example.converterapp.ui

import android.app.Application
import android.content.Context
import android.net.*
import android.os.Build
import androidx.annotation.RequiresPermission
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import javax.inject.Inject

class ConnectivityObservable constructor(
    private val connectivityManager: ConnectivityManager
) {

    @RequiresPermission(android.Manifest.permission.ACCESS_NETWORK_STATE)
    @Inject
    constructor(application: Application) : this(
        application.getSystemService(Context.CONNECTIVITY_SERVICE)
                as ConnectivityManager
    )

    private val networkStateSubject = BehaviorSubject.create<Boolean>()

    private val networkCallback = object : ConnectivityManager.NetworkCallback() {

        override fun onAvailable(network: Network?) {
            networkStateSubject.onNext(true)
        }

        override fun onLost(network: Network?) {
            networkStateSubject.onNext(false)
        }
    }


    fun registerNetworkStateObserver() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val activeNetwork: NetworkCapabilities? = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
            val capability = activeNetwork?.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED) ?: false
            networkStateSubject.onNext(capability)
        } else {
            val activeNetwork: NetworkInfo? = connectivityManager.activeNetworkInfo
            networkStateSubject.onNext(activeNetwork?.isConnectedOrConnecting == true)
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            connectivityManager.registerDefaultNetworkCallback(networkCallback)
        } else {
            val builder = NetworkRequest.Builder()
            connectivityManager.registerNetworkCallback(builder.build(), networkCallback)
        }
    }

    fun unregisterNetworkStateObserver() {
        connectivityManager.unregisterNetworkCallback(networkCallback)
    }

    fun networkState(): Observable<Boolean> {
        return networkStateSubject.hide()
    }
}