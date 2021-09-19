package com.prototype.exam.data.repository

import androidx.lifecycle.LiveData
import com.prototype.exam.data.api.ApiHelper
import com.prototype.exam.data.db.ForecastDao
import com.prototype.exam.data.model.forecast.ForecastItem
import com.prototype.exam.data.model.forecast.ForecastResponse
import retrofit2.Response
import javax.inject.Inject


class UserRepositoryImpl  @Inject constructor(private val apiHelper: ApiHelper, private val dao: ForecastDao) : UserRepository {

    override suspend fun getForecasts(id: String, unit: String, appId: String): Response<ForecastResponse> {
        return apiHelper.getForecasts(id, unit, appId)
    }

    override suspend fun getForecast(id: String, unit: String, appId: String) : Response<ForecastItem> {
        return  apiHelper.getForecast(id, unit, appId)
    }

    override fun getLiveLocalForecasts(): LiveData<List<ForecastItem>> {
        return dao.getAllLiveForecasts()
    }

    override fun getLocalForecasts(): List<ForecastItem> {
        return dao.getAllForecasts()
    }

    override fun getLocalForecast(id: Int): ForecastItem {
        return dao.getForecast(id)
    }

    override fun addForecasts(list: List<ForecastItem>) {
        return dao.addForecastList(list)
    }

    override fun addForecast(item: ForecastItem) {
        return dao.addForecast(item)
    }

    override fun updateForecastFavorite(id: String, isToggled: Boolean) {
            dao.updateForecastFavorite(id, isToggled)
        }
}