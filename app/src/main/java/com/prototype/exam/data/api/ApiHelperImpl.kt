package com.prototype.exam.data.api

import com.prototype.exam.data.model.Users
import kotlinx.coroutines.Deferred
import retrofit2.Response
import javax.inject.Inject


class ApiHelperImpl @Inject constructor(private val apiService: ApiService) : ApiHelper {
    override suspend fun getUsers(): Response<Users> = apiService.getUsers()
    override suspend fun getUsers2(): Deferred<Response<Users>> = apiService.getUsers2()

}
