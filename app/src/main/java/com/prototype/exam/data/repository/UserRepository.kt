package com.prototype.exam.data.repository

import androidx.lifecycle.LiveData
import com.prototype.exam.data.api.ResultWrapper
import com.prototype.exam.data.model.User
import com.prototype.exam.data.model.Users

interface UserRepository {
    fun getLocalUsers(): LiveData<List<User>>
    fun addUsers(list: List<User>)
    fun addUser(user: User)
    fun getUser(id: Int): User
    suspend fun getUsers(): ResultWrapper<Users?>
}

