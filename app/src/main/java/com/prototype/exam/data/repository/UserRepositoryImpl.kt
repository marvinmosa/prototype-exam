package com.prototype.exam.data.repository

import androidx.lifecycle.LiveData
import com.prototype.exam.data.api.ApiHelper
import com.prototype.exam.data.api.ErrorManagerHelperImpl
import com.prototype.exam.data.api.ResultWrapper
import com.prototype.exam.data.db.UserDao
import com.prototype.exam.data.model.User
import com.prototype.exam.data.model.Users
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response
import javax.inject.Inject


class UserRepositoryImpl @Inject constructor(
    private val apiHelper: ApiHelper,
    private val dao: UserDao
) : UserRepository {
    override fun addUsers(list: List<User>) {
        return dao.addUsers(list)
    }

    override fun getLocalUsers(): LiveData<List<User>> {
        return dao.getUsers()
    }

    override fun addUser(user: User) {
        TODO("Not yet implemented")
    }

    override fun getUser(id: Int): User {
        return dao.getUser(id)
    }

    override suspend fun getUsers(): ResultWrapper<Users?> {
        return withContext(Dispatchers.IO) {
            val errorHelper = ErrorManagerHelperImpl()
            errorHelper.dataCall() {
                apiHelper.getUsers().body()
            }
        }
    }
}