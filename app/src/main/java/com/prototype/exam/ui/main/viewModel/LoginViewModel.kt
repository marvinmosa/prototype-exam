package com.prototype.exam.ui.main.viewModel

import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.prototype.exam.R
import com.prototype.exam.data.repository.LoginRepositoryImpl
import com.prototype.exam.ui.base.BaseViewModel
import com.prototype.exam.ui.main.view.login.LoggedInUserView
import com.prototype.exam.ui.main.view.login.LoginFormState
import com.prototype.exam.ui.main.view.login.LoginResult
import com.prototype.exam.utils.Status
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class LoginViewModel @Inject constructor(private val loginRepository: LoginRepositoryImpl) :
    BaseViewModel() {

    private val _loginForm = MutableLiveData<LoginFormState>()
    val loginFormState: LiveData<LoginFormState> = _loginForm

    private val _loginResult = MutableLiveData<LoginResult>()
    val loginResult: LiveData<LoginResult> = _loginResult

    init {
        addDummyUser()
    }

    fun isLoggedIn(): Boolean {
        return loginRepository.isLoggedIn
    }

    fun login(username: String, password: String) {
        // can be launched in a separate asynchronous job
        viewModelScope.launch {
            //loading

            withContext(Dispatchers.IO) {
                val result = loginRepository.login(username, password)

                withContext(Dispatchers.Main) {
                    if (result.data === Status.SUCCESS) {
                        _loginResult.value =
                            LoginResult(success = LoggedInUserView(displayName = "Hello"))
                    } else {
                        System.out.println(result.message)
                        _loginResult.value = LoginResult(error = R.string.login_failed)
                    }
                }
            }
        }
    }

    fun loginDataChanged(username: String, password: String) {
        if (!isUserNameValid(username)) {
            _loginForm.value = LoginFormState(usernameError = R.string.invalid_username)
        } else if (!isPasswordValid(password)) {
            _loginForm.value = LoginFormState(passwordError = R.string.invalid_password)
        } else {
            _loginForm.value = LoginFormState(isDataValid = true)
        }
    }

    // A placeholder username validation check
    private fun isUserNameValid(username: String): Boolean {
        return if (username.contains('@')) {
            Patterns.EMAIL_ADDRESS.matcher(username).matches()
        } else {
            username.isNotBlank()
        }
    }

    // A placeholder password validation check
    private fun isPasswordValid(password: String): Boolean {
        return password.length > 5
    }

    private fun addDummyUser() {
        viewModelScope.launch(Dispatchers.IO) {
            loginRepository.addDummyUser()
        }
    }
}