package com.prototype.exam.ui.main.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.prototype.exam.data.model.User
import com.prototype.exam.data.model.forecast.ForecastItem
import com.prototype.exam.data.repository.Repository
import com.prototype.exam.data.repository.RepositoryImpl
import com.prototype.exam.ui.base.BaseViewModel
import com.prototype.exam.utils.NetworkHelper
import com.prototype.exam.utils.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject


class UserDetailViewModel @Inject constructor(
    private val repository: Repository,
    private val networkHelper: NetworkHelper
) : BaseViewModel() {

//    init {
//        fetchData()
//    }

    private val forecast = MutableLiveData<Result<ForecastItem>>()
    val forecasts: LiveData<Result<ForecastItem>>
        get() = forecast

    private val _users = MutableLiveData<Result<User>>()
    val users: LiveData<Result<User>>
        get() = _users

    fun fetchData() {
        viewModelScope.launch {
            if (!networkHelper.isNetworkConnected()) {
                 forecast.postValue(Result.error(null, "No internet connection"))
            }
            withContext(Dispatchers.IO) {

            }

        }
    }



    fun fetchForecast(locationId: String?) {
        locationId?.let {
            viewModelScope.launch {
                forecast.postValue(Result.loading(null))
                if (networkHelper.isNetworkConnected()) {
                    withContext(Dispatchers.IO) {
                        try {
                            val response = repository.getForecast(it)
                            if (response.isSuccessful) {
                                val localForecastItem =
                                    repository.getLocalForecast(locationId.toInt())
                                response.body()?.let {
                                    it.favorite = localForecastItem.favorite
                                    repository.addForecast(it)
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
                repository.updateForecastFavorite(it, hasToggled)
            }
        }
    }
}