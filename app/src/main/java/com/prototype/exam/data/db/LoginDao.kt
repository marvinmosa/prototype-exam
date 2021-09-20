package com.prototype.exam.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.prototype.exam.data.model.LoginUser
import com.prototype.exam.data.model.User

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
@Dao
interface LoginDao {
    @Query("SELECT * FROM login ORDER BY id")
    fun getLoginUsers(): List<LoginUser>

    @Query("SELECT * FROM login WHERE username = :username")
    fun getLoginUser(username: String): LoginUser

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addLoginUsers(list: List<LoginUser>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addLoginUser(loginUser: LoginUser)
}