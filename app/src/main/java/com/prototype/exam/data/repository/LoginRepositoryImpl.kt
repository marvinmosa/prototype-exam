package com.prototype.exam.data.repository

import android.content.SharedPreferences
import com.prototype.exam.data.db.LoginDao
import com.prototype.exam.data.model.LoginUser
import com.prototype.exam.utils.Result
import com.prototype.exam.utils.Constants.SHARED_PREFERENCE_LOGIN_KEY
import com.prototype.exam.utils.Status
import javax.inject.Inject

class LoginRepositoryImpl @Inject constructor(
    private val dao: LoginDao,
    sharedPref: SharedPreferences
) : LoginRepository {

    private val _loginSharedPref = sharedPref
    private var _user: String? = null
        private set

    val isLoggedIn: Boolean
        get() = _user != null

    init {
        // If user credentials will be cached in local storage, it is recommended it be encrypted
        // @see https://developer.android.com/training/articles/keystore
        _user = _loginSharedPref.getString(SHARED_PREFERENCE_LOGIN_KEY, null)
    }

    override fun logout() {
        _loginSharedPref.edit().putString(SHARED_PREFERENCE_LOGIN_KEY, null).apply()
        _user = null
    }

    override fun login(username: String, password: String, isSaved: Boolean): Result<Status> {
        // handle login
        val loginUser = dao.getLoginUser(username)

        if(loginUser === null) {
            return  Result.error(Status.ERROR, "User does not exist")
        }

        return if(loginUser.password.compareTo(password)==0) {
            if(isSaved) setLoggedInUser(loginUser.id.toString())
            Result.success(Status.SUCCESS)
        } else {
            Result.error(Status.ERROR, "invalid credentials")
        }
    }

    override fun setLoggedInUser(uid: String) {
        _user = uid
        _loginSharedPref.edit().putString(SHARED_PREFERENCE_LOGIN_KEY, _user).apply()
        // If user credentials will be cached in local storage, it is recommended it be encrypted
        // @see https://developer.android.com/training/articles/keystore
    }

    override fun addDummyUser() {
        val user = LoginUser(1,"marvinmosa@test.com", "test123")
        dao.addLoginUser(user)
    }
}