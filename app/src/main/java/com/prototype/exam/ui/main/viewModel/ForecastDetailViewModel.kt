package com.prototype.exam.ui.main.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.prototype.exam.data.model.ForecastItem
import com.prototype.exam.data.repository.MainRepository
import com.prototype.exam.ui.base.BaseViewModel
import com.prototype.exam.utils.NetworkHelper
import com.prototype.exam.utils.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject


class ForecastDetailViewModel @Inject constructor(
    private val mainRepository: MainRepository,
    private val networkHelper: NetworkHelper
) : BaseViewModel() {


    private val forecast = MutableLiveData<Result<ForecastItem>>()
    val forecasts: LiveData<Result<ForecastItem>>
        get() = forecast


    fun fetchForecast(locationId: String?) {
        locationId?.let {
            viewModelScope.launch {
                forecast.postValue(Result.loading(null))
                if (networkHelper.isNetworkConnected()) {
                    withContext(Dispatchers.IO) {
                        try {
                            val response = mainRepository.getForecast(it)
                            if (response.isSuccessful) {
                                val localForecastItem =
                                    mainRepository.getLocalForecast(locationId.toInt())
                                response.body()?.let {
                                    it.favorite = localForecastItem.favorite
                                    mainRepository.addForecast(it)
                                    forecast.postValue(Result.success(it))
                                }
                            } else forecast.postValue(
                                Result.error(
                                    null,
                                    response.message().toString()
                                )
                            )
                        } catch (e: Exception) {
                            forecast.postValue(Result.error(null, e.message.toString()))
                        }
                    }
                } else forecast.postValue(Result.error(null, "No internet connection"))
            }
        } ?: kotlin.run {
            forecast.postValue(Result.error(null, "Invalid City Id"))
        }
    }

    fun onToggleFavorite(locationId: String?, hasToggled: Boolean) {
        locationId?.let {
            viewModelScope.launch(Dispatchers.IO) {
                mainRepository.updateForecastFavorite(it, hasToggled)
            }
        }
    }
}