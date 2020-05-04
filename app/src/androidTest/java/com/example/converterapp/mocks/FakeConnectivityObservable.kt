package com.example.converterapp.mocks

import com.example.converterapp.ui.IConnectivityObervable
import io.reactivex.Observable

class FakeConnectivityObservable :
    IConnectivityObervable {

    var isAvailable = true

    override fun registerNetworkStateObserver() {
        //not needed
    }

    override fun unregisterNetworkStateObserver() {
        //not needed
    }

    override fun networkState(): Observable<Boolean> {
        return Observable.just(isAvailable)
    }
}