package com.prototype.exam.data.repository

import com.prototype.exam.data.api.ApiHelper
import com.prototype.exam.data.db.ForecastDao
import com.prototype.exam.data.model.ForecastItem
import javax.inject.Inject


class MainRepository @Inject constructor(private val apiHelper: ApiHelper, private val dao: ForecastDao) {

    suspend fun getForecasts(id: String, unit: String = API_UNIT, appId: String = API_APP_ID) = apiHelper.getForecasts(id, unit, appId)

    suspend fun getForecast(id: String, unit: String = API_UNIT, appId: String = API_APP_ID) = apiHelper.getForecast(id, unit, appId)

    fun getLiveLocalForecasts() = dao.getAllLiveForecasts()

    fun getLocalForecasts() = dao.getAllForecasts()

    fun getLocalForecast(id: Int) = dao.getForecast(id)

    fun addForecasts(list: List<ForecastItem>) = dao.addForecastList(list)

    fun addForecast(item: ForecastItem) = dao.addForecast(item)

    fun updateForecastFavorite(id: String, isToggled: Boolean) = dao.updateForecastFavorite(id, isToggled)

    companion object{
        const val API_APP_ID = "8beca4dc58974504cca73181ee8ca127"
        const val API_UNIT = "metric"
    }
}