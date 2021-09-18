package com.prototype.exam.data.api

import com.prototype.exam.data.model.ForecastItem
import com.prototype.exam.data.model.ForecastResponse
import retrofit2.Response
import javax.inject.Inject


class ApiHelperImpl @Inject constructor(private val apiService: ApiService) : ApiHelper {

    override suspend fun getForecasts(id: String, unit: String, appId: String): Response<ForecastResponse> = apiService.getForecasts(id, unit, appId)
    override suspend fun getForecast(id: String, unit: String, appId: String): Response<ForecastItem> = apiService.getForecast(id, unit, appId)
}
