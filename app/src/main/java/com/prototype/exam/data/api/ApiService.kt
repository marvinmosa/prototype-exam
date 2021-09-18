package com.prototype.exam.data.api

import com.prototype.exam.data.model.ForecastItem
import com.prototype.exam.data.model.ForecastResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query


interface ApiService {

    @GET("group")
    suspend fun getForecasts(@Query("id") id: String, @Query("units") unit: String, @Query("appid") appId: String): Response<ForecastResponse>

    @GET("weather")
    suspend fun getForecast(@Query("id") id: String, @Query("units") unit: String, @Query("appid") appId: String): Response<ForecastItem>
}