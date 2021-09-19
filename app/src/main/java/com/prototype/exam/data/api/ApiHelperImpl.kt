package com.prototype.exam.data.api

import com.prototype.exam.data.model.Users
import com.prototype.exam.data.model.forecast.ForecastItem
import com.prototype.exam.data.model.forecast.ForecastResponse
import retrofit2.Response
import javax.inject.Inject


class ApiHelperImpl @Inject constructor(private val apiService: ApiService) : ApiHelper {

    override suspend fun getForecasts(
        id: String,
        unit: String,
        appId: String
    ): Response<ForecastResponse> = apiService.getForecasts(id, unit, appId)

    override suspend fun getForecast(
        id: String,
        unit: String,
        appId: String
    ): Response<ForecastItem> = apiService.getForecast(id, unit, appId)

    override suspend fun getUsers(): Response<Users> = apiService.getUsers()
}
