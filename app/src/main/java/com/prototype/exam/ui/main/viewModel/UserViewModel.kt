package com.prototype.exam.ui.main.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.prototype.exam.data.model.User
import com.prototype.exam.data.repository.UserRepository
import com.prototype.exam.ui.base.BaseViewModel
import com.prototype.exam.utils.NetworkHelper
import com.prototype.exam.utils.Result
import kotlinx.coroutines.*
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext


class UserViewModel @Inject constructor(
    private val repository: UserRepository,
    private val networkHelper: NetworkHelper
) : BaseViewModel() {
    //create a new Job
    private val parentJob = Job()
    //create a coroutine context with the job and the dispatcher
    private val coroutineContext : CoroutineContext get() = parentJob + Dispatchers.Default
    //create a coroutine scope with the coroutine context
    private val scope = CoroutineScope(coroutineContext)

    private val _users = MutableLiveData<Result<List<User>>>()
    val users: LiveData<Result<List<User>>>
        get() = _users

    private val _users2 = MutableLiveData<MutableList<User>?>()
    val users2: LiveData<MutableList<User>?>
        get() = _users2

    init {
        //fetchUsers()
        fetchUsers2()
    }

     private fun fetchUsers2() {
        scope.launch {
            val latestUsers = repository.getUsers2()
                _users2.postValue(latestUsers)
        }
    }

    internal fun fetchUsers() {
        viewModelScope.launch {
            _users.postValue(Result.loading(null))
            if (!networkHelper.isNetworkConnected()) {
                _users.postValue(Result.error(null, "No internet connection"))
            } else {
                withContext(Dispatchers.IO) {
                    try {
                        val response = repository.getUsers()
                        if (response.isSuccessful) {
                            response.body()?.let {
                                repository.addUsers(it)
                                _users.postValue(Result.success(it))
                            }
                        } else {
                            _users.postValue(Result.error(null, response.message().toString()))
                        }
                    } catch (e: Exception) {
                        _users.postValue(Result.error(null, e.message.toString()))
                    }
                }
            }
        }
    }

    fun getLiveLocalUsers(): LiveData<List<User>> {
        return repository.getLocalUsers()
    }
}