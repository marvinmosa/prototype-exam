package com.prototype.exam.data.repository

import androidx.lifecycle.LiveData
import com.prototype.exam.data.model.forecast.ForecastItem
import com.prototype.exam.data.model.forecast.ForecastResponse
import retrofit2.Response

interface UserRepository {
    suspend fun getForecasts(id: String, unit: String = API_UNIT, appId: String = API_APP_ID) : Response<ForecastResponse>

    suspend fun getForecast(id: String, unit: String = API_UNIT, appId: String = API_APP_ID): Response<ForecastItem>

    fun getLiveLocalForecasts() : LiveData<List<ForecastItem>>

    fun getLocalForecasts() : List<ForecastItem>

    fun getLocalForecast(id: Int) : ForecastItem

    fun addForecasts(list: List<ForecastItem>)

    fun addForecast(item: ForecastItem)

    fun updateForecastFavorite(id: String, isToggled: Boolean)

    companion object{
        const val API_APP_ID = "8beca4dc58974504cca73181ee8ca127"
        const val API_UNIT = "metric"
    }
}