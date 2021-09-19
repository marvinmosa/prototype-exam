package com.prototype.exam.data.repository

import androidx.lifecycle.LiveData
import com.prototype.exam.data.model.User
import com.prototype.exam.data.model.Users
import retrofit2.Response

interface UserRepository {
    suspend fun getUsers(): Response<Users>

    fun getLocalUsers(): LiveData<List<User>>
    fun addUsers(list: List<User>)
    fun addUser(user: User)
    fun getUser(id: Int): User
}