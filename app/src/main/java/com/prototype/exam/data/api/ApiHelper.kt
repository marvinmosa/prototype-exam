package com.prototype.exam.data.api

import com.prototype.exam.data.model.Users
import retrofit2.Response

interface ApiHelper {
    suspend fun getUsers(): Response<Users>
}