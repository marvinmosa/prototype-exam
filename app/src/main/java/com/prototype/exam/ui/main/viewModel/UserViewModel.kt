package com.prototype.exam.ui.main.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.prototype.exam.data.api.ResultWrapper
import com.prototype.exam.data.model.User
import com.prototype.exam.data.repository.UserRepository
import com.prototype.exam.ui.base.BaseViewModel
import com.prototype.exam.utils.NetworkHelper
import com.prototype.exam.utils.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject


class UserViewModel @Inject constructor(
    private val repository: UserRepository,
    private val networkHelper: NetworkHelper
) : BaseViewModel() {
    private val _users = MutableLiveData<Result<List<User>>>()
    val users: LiveData<Result<List<User>>>
        get() = _users

    init {
        fetchUsers()
    }

    internal fun fetchUsers() {
        viewModelScope.launch {
            _users.postValue(Result.loading(null))
            if (!networkHelper.isNetworkConnected()) {
                _users.postValue(Result.error(null, "No internet connection"))
            } else {
                withContext(Dispatchers.IO) {
                    when (val response = repository.getUsers()) {
                        is ResultWrapper.NetworkError -> {
                            _users.postValue(Result.error(null, "Something went wrong!!"))
                        }
                        is ResultWrapper.GenericError -> {
                            response.error?.let {
                                _users.postValue(Result.error(null, it.errorMessage))
                            } ?: run {
                                _users.postValue(Result.error(null, "Something went wrong!!"))
                            }
                        }
                        is ResultWrapper.Success -> {
                            val users = response.value
                            users?.let { repository.addUsers(it.toList()) }
                            _users.postValue(Result.success(users))
                        }
                    }
                }
            }
        }
    }

    fun getLiveLocalUsers(): LiveData<List<User>> {
        return repository.getLocalUsers()
    }
}