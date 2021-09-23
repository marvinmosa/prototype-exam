package com.prototype.exam.ui.main.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.prototype.exam.data.model.User
import com.prototype.exam.data.repository.UserRepository
import com.prototype.exam.ui.base.BaseViewModel
import com.prototype.exam.utils.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject


class UserDetailViewModel @Inject constructor(private val repository: UserRepository) :
    BaseViewModel() {

    private val _user = MutableLiveData<Result<User>>()
    val user: LiveData<Result<User>>
        get() = _user

    fun fetchData(id: Int) {
        viewModelScope.launch {
            _user.postValue(Result.loading(null))

            withContext(Dispatchers.IO) {
                val item = repository.getUser(id)
                _user.postValue(Result.success(item))
            }
        }
    }
}