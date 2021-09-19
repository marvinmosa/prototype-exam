package com.prototype.exam.data.api

import com.prototype.exam.data.model.Users
import retrofit2.Response
import javax.inject.Inject


class ApiHelperImpl @Inject constructor(private val apiService: ApiService) : ApiHelper {
    override suspend fun getUsers(): Response<Users> = apiService.getUsers()
}
