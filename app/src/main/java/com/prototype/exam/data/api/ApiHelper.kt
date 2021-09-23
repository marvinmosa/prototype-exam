package com.prototype.exam.data.api

import com.prototype.exam.data.model.Users
import kotlinx.coroutines.Deferred
import retrofit2.Response

interface ApiHelper {
    suspend fun getUsers(): Response<Users>
    suspend fun getUsers2(): Deferred<Response<Users>>
}