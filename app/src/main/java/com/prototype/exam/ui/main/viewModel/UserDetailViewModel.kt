package com.prototype.exam.ui.main.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.prototype.exam.data.model.User
import com.prototype.exam.data.model.forecast.ForecastItem
import com.prototype.exam.data.repository.UserRepository
import com.prototype.exam.ui.base.BaseViewModel
import com.prototype.exam.utils.NetworkHelper
import com.prototype.exam.utils.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject


class UserDetailViewModel @Inject constructor(
    private val repository: UserRepository,
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




}