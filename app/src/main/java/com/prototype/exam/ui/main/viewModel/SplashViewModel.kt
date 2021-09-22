package com.prototype.exam.ui.main.viewModel

import com.prototype.exam.data.repository.LoginRepositoryImpl
import com.prototype.exam.ui.base.BaseViewModel
import javax.inject.Inject

class SplashViewModel @Inject constructor(private val loginRepository: LoginRepositoryImpl) :
    BaseViewModel() {

    fun isLoggedIn(): Boolean {
        return loginRepository.isLoggedIn
    }
}