package com.prototype.exam.data.repository

import com.prototype.exam.utils.Result
import com.prototype.exam.utils.Status

interface LoginRepository {
    fun logout()
    fun login(username: String, password: String, isSaved: Boolean): Result<Status>
    fun setLoggedInUser(uid: String)
    fun addDummyUser()
}