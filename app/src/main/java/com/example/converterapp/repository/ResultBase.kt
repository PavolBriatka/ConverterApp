package com.example.converterapp.repository

sealed class ResultBase<out T: Any> {

    class Success< out T: Any>(val result: T): ResultBase<T>()
    class Error(val errorType: ErrorType): ResultBase<Nothing>()

    enum class ErrorType {
        NETWORK_ERROR,
        DATABASE_ERROR
    }
}