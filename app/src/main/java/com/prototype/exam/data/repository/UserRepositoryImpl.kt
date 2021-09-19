package com.prototype.exam.data.repository

import androidx.lifecycle.LiveData
import com.prototype.exam.data.api.ApiHelper
import com.prototype.exam.data.db.UserDao
import com.prototype.exam.data.model.User
import com.prototype.exam.data.model.Users
import retrofit2.Response
import javax.inject.Inject


class UserRepositoryImpl @Inject constructor(
    private val apiHelper: ApiHelper,
    private val dao: UserDao
) : UserRepository {

    override suspend fun getUsers(): Response<Users> {
        return apiHelper.getUsers()
    }

    override fun addUsers(list: List<User>) {
        return dao.addUsers(list)
    }

    override fun getLocalUsers(): LiveData<List<User>> {
        return dao.getUsers()
    }

    override fun addUser(user: User) {
        TODO("Not yet implemented")
    }
}