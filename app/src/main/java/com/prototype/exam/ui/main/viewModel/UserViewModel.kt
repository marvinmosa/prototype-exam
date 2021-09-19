package com.prototype.exam.ui.main.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.prototype.exam.data.model.forecast.ForecastItem
import com.prototype.exam.data.repository.Repository
import com.prototype.exam.ui.base.BaseViewModel
import com.prototype.exam.utils.NetworkHelper
import com.prototype.exam.utils.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject


class UserViewModel @Inject constructor(
    private val repository: Repository,
    private val networkHelper: NetworkHelper
) : BaseViewModel() {

    private val forecast = MutableLiveData<Result<List<ForecastItem>>>()
    val forecasts: LiveData<Result<List<ForecastItem>>>
        get() = forecast

    init {
        fetchForecasts()
    }

    fun getLiveLocalForecasts(): LiveData<List<ForecastItem>> {
        return repository.getLiveLocalForecasts()
    }

    fun fetchForecasts() {
        viewModelScope.launch {
            forecast.postValue(Result.loading(null))
            if (networkHelper.isNetworkConnected()) {
                withContext(Dispatchers.IO) {
                    val locationList = LOCATION_LIST.joinToString(separator = ",")
                    try {
                        val response = repository.getForecasts(locationList)
                        if (response.isSuccessful) {
                            response.body()?.let { remoteResponse ->
                                val localForecasts = repository.getLocalForecasts()
                                val remoteForecasts = remoteResponse.forecastList

                                localForecasts.map { localForecastItem ->
                                    remoteForecasts.firstOrNull {
                                        localForecastItem.id.equals(it.id, ignoreCase = true)
                                    }?.let { it.favorite = localForecastItem.favorite }
                                }
                                repository.addForecasts(remoteForecasts)
                                forecast.postValue(Result.success(remoteForecasts))
                            }
                        } else {
                            forecast.postValue(Result.error(null, response.message().toString()))
                        }
                    } catch (e: Exception) {
                        forecast.postValue(Result.error(null, e.message.toString()))
                    }
                }
            } else {
                forecast.postValue(Result.error(null, "No internet connection"))
            }
        }
    }

    companion object {
        val LOCATION_LIST = listOf("1701668", "1835848", "3067696")
    }
}