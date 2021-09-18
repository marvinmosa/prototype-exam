package com.prototype.exam.data.api

import com.prototype.exam.data.model.ForecastItem
import com.prototype.exam.data.model.ForecastResponse
import retrofit2.Response

interface ApiHelper {

    suspend fun getForecasts(id: String, unit: String, appId: String): Response<ForecastResponse>
    suspend fun getForecast(id: String, unit: String, appId: String): Response<ForecastItem>
}