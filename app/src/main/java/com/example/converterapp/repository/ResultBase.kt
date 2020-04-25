package com.example.converterapp.repository

sealed class ResultBase<out T: Any> {

    class Success< out T: Any>(val result: T): ResultBase<T>()
    object Error: ResultBase<Nothing>()
}