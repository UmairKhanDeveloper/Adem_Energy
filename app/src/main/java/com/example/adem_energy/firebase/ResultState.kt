package com.example.adem_energy.firebase

sealed class ResultState<out T> {
    object Loading : ResultState<Nothing>()
    data class Success<T>(val response: T) : ResultState<T>()
    data class Error(val exception: Throwable) : ResultState<Nothing>()
}
