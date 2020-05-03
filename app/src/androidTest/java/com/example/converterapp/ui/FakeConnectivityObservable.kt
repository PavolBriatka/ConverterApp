package com.example.converterapp.ui

import io.reactivex.Observable

class FakeConnectivityObservable : IConnectivityObervable {

    override fun registerNetworkStateObserver() {
        //not needed
    }

    override fun unregisterNetworkStateObserver() {
        //not needed
    }

    override fun networkState(): Observable<Boolean> {
        return Observable.just(true)
    }
}