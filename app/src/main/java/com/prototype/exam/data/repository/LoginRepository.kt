package com.prototype.exam.data.repository

import com.prototype.exam.ui.main.view.login.data.Result
import com.prototype.exam.ui.main.view.login.data.model.LoggedInUser

interface LoginRepository {
    fun logout()
    fun login(username: String, password: String): Result<LoggedInUser>
    fun setLoggedInUser(loggedInUser: LoggedInUser)
}