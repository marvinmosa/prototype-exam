package com.prototype.exam.data.api

sealed class ResultWrapper<out T> {
    data class Success<out T>(val value: T): ResultWrapper<T>()
    data class GenericError(val code: Int? = null, val error: ErrorResponse? = null): ResultWrapper<Nothing>()
    data class ErrorResponse(
        val errorMessage: String,
        val causes: Map<String, String> = emptyMap()
    )
    object NetworkError: ResultWrapper<Nothing>()
}