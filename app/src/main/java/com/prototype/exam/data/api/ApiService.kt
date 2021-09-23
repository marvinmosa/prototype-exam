package com.prototype.exam.data.api

import com.prototype.exam.data.model.Users
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query


interface ApiService {
    @GET("users")
    suspend fun getUsers(): Response<Users>

    @GET("users")
    fun getUsers2() : Deferred<Response<Users>>
}