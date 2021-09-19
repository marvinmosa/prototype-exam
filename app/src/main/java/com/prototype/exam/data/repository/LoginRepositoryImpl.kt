package com.prototype.exam.data.repository

import android.content.SharedPreferences
import com.prototype.exam.data.db.LoginDao
import com.prototype.exam.ui.main.view.login.data.Result
import com.prototype.exam.ui.main.view.login.data.model.LoggedInUser
import com.prototype.exam.utils.Constants.SHARED_PREFERENCE_LOGIN_KEY
import javax.inject.Inject

class LoginRepositoryImpl @Inject constructor(
    private val dao: LoginDao,
    private val sharedPref: SharedPreferences
) : LoginRepository {

    private val _loginSharedPref = sharedPref

    // in-memory cache of the loggedInUser object
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

    override fun login(username: String, password: String): Result<LoggedInUser> {
        // handle login
        val result = dao.login(username, password)

        if (result is Result.Success) {
            setLoggedInUser(result.data)
        }

        return result
    }

    override fun setLoggedInUser(loggedInUser: LoggedInUser) {
        _user = loggedInUser.displayName
        _loginSharedPref.edit().putString(SHARED_PREFERENCE_LOGIN_KEY, _user).apply()
        // If user credentials will be cached in local storage, it is recommended it be encrypted
        // @see https://developer.android.com/training/articles/keystore
    }
}