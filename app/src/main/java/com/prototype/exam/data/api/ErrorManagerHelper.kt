package com.prototype.exam.data.api

interface ErrorManagerHelper {
    suspend fun <T> dataCall(dataCall: suspend () -> T): ResultWrapper<T>
}