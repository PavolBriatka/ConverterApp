package com.example.converterapp.ui

import io.reactivex.Observable

interface IConnectivityObervable {

    fun registerNetworkStateObserver()
    fun unregisterNetworkStateObserver()
    fun networkState(): Observable<Boolean>
}